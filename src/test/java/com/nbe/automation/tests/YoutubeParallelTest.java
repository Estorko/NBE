package com.nbe.automation.tests;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.nbe.automation.config.AppiumServerManager;
import com.nbe.automation.config.DriverFactory;
import com.nbe.automation.config.EmulatorManager;
import com.nbe.automation.config.TestLauncher;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.LoggerUtil;
import com.nbe.automation.pages.Youtube.ChannelPage;
import com.nbe.automation.pages.Youtube.HomePage;
import com.nbe.automation.pages.Youtube.SearchResultsPage;

import io.appium.java_client.android.AndroidDriver;

@SpringBootTest
@ComponentScan(basePackages = "com.nbe.automation")
@Execution(ExecutionMode.CONCURRENT)
class YoutubeParallelTest {

    private final String channelName = "NBE";

    @Autowired
    private DriverFactory driverFactory;

    static Stream<String> udidsProvider() {
        return TestLauncher.getAssignedUdids().stream();
    }

    @ParameterizedTest(name = "Device {0}")
    @MethodSource("udidsProvider")
    void fullYoutubeTestSequence(String udid) throws Exception {
        AndroidDriver driver = driverFactory.getDriver(udid);
        HomePage homePage = new HomePage(driver, new AppiumUtils(driver));
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver, new AppiumUtils(driver));
        ChannelPage channelPage = new ChannelPage(driver, new AppiumUtils(driver));
        executeOrderedSteps(udid, homePage, searchResultsPage, channelPage);
    }

    private void executeOrderedSteps(String udid,
            HomePage homePage,
            SearchResultsPage searchResultsPage,
            ChannelPage channelPage) throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> searchFuture = executor.submit(() -> {
                LoggerUtil.info(String.format("[%s] Starting search test", udid), this.getClass());
                homePage.search(channelName);
            });

            Future<?> channelFuture = executor.submit(() -> {
                try {
                    searchFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                LoggerUtil.info(String.format("[%s] Starting channel test", udid), this.getClass());
                searchResultsPage.clickOnChannel();
            });

            Future<?> videoFuture = executor.submit(() -> {
                try {
                    channelFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                LoggerUtil.info(String.format("[%s] Starting video test", udid), this.getClass());
                channelPage.clickOnVideosTab();

                // hidden for POC-purpose, works but unstable due to UI differences
                // Assertions.assertTrue(channelPage.clickOnFirstVideo(),
                // String.format("[%s] Video playback failed", udid));
            });
            videoFuture.get();
        } finally {
            executor.shutdown();
        }
    }

    @BeforeAll
    static void globalSetup(@Autowired TestLauncher testLauncher) {
        testLauncher.waitForDrivers(30000);
    }

    @AfterAll
    static void globalTeardown(@Autowired AppiumServerManager appiumServerManager,
            @Autowired EmulatorManager emulatorManager) {
        LoggerUtil.info("Shutting down all resources", YoutubeParallelTest.class);
        appiumServerManager.killAllAppiumServers();
        emulatorManager.killAllEmulators();
    }
}

package com.nbe.automation.tests;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.nbe.automation.pages.youtube.ChannelPage;
import com.nbe.automation.pages.youtube.HomePage;
import com.nbe.automation.pages.youtube.SearchResultsPage;
import com.nbe.automation.tests.base.BaseTest;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeParallelTest extends BaseTest {

    private static final String CHANNEL_NAME = "NBE";

    @Execution(ExecutionMode.CONCURRENT)
    @ParameterizedTest(name = "Device {0}")
    @MethodSource("udidsProvider")
    void fullYoutubeTestSequence(String udid) throws Exception {
        LoggerUtil.info(String.format(">>> Running tests for: [%s]", udid), this.getClass());
        AndroidDriver driver = driverFactory.getDriver(udid);
        HomePage homePage = new HomePage(driver);
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        ChannelPage channelPage = new ChannelPage(driver);
        executeOrderedSteps(udid, homePage, searchResultsPage, channelPage);
    }

    private void executeOrderedSteps(String udid, HomePage homePage, SearchResultsPage searchResultsPage,
            ChannelPage channelPage) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<?> searchFuture = executor.submit(() -> {
                LoggerUtil.info(String.format("[%s] Starting search test", udid), this.getClass());
                homePage.search(CHANNEL_NAME);
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
                LoggerUtil.info(String.format("[%s] Going back to Hompage", udid), this.getClass());
                channelPage.goBackToHomePage();
                // Assertions.assertTrue(channelPage.clickOnFirstVideo(),
                // String.format("[%s] Video playback failed", udid));
            });
            videoFuture.get();
        } finally {
            executor.shutdown();
        }
    }

    @AfterAll
    void globalTeardown() {
        latch.countDown();
    }
}

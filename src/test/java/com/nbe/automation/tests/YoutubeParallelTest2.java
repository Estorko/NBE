package com.nbe.automation.tests;

import java.util.concurrent.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.pages.youtube.ChannelPage;
import com.nbe.automation.pages.youtube.HomePage;
import com.nbe.automation.pages.youtube.SearchResultsPage;
import com.nbe.automation.tests.base.BaseTest;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeParallelTest2 extends BaseTest {

    private static final String CHANNEL_NAME = "NBE";
    private ChannelPage channelPage;

    @Execution(ExecutionMode.CONCURRENT)
    @ParameterizedTest(name = "Device {0}")
    @MethodSource("udidsProvider")
    void fullYoutubeTestSequence(String udid) throws Exception {
        AndroidDriver driver = driverFactory.getDriver(udid);
        HomePage homePage = new HomePage(driver, new BasePage(driver));
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver, new BasePage(driver));
        channelPage = new ChannelPage(driver, new BasePage(driver));
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

package com.nbe.automation.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.nbe.automation.config.AppiumServerManager;
import com.nbe.automation.config.DriverFactory;
import com.nbe.automation.config.EmulatorManager;
import com.nbe.automation.config.TestLauncher;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.LoggerUtil;
import com.nbe.automation.pages.Youtube.*;

import io.appium.java_client.android.AndroidDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ComponentScan(basePackages = "com.nbe.automation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.CONCURRENT)
public class YoutubeTest {

    private static ChannelPage channelPage;
    private static HomePage homePage;
    private static SearchResultsPage searchResultsPage;

    private final String CHANNEL_NAME = "NBE";

    @Autowired
    private EmulatorManager emulatorManager;

    @Autowired
    private AppiumServerManager appiumServerManager;

    @BeforeAll
    static void setUp(@Autowired TestLauncher testLauncher,
            @Autowired DriverFactory driverFactory,
            @Autowired EmulatorManager emulatorManager) {
        testLauncher.waitForDrivers();
        String udid = emulatorManager.getEmulatorUdid();
        AndroidDriver driver = driverFactory.getDriver(udid);
        homePage = new HomePage(driver, new AppiumUtils(driver));
        searchResultsPage = new SearchResultsPage(driver, new AppiumUtils(driver));
        channelPage = new ChannelPage(driver, new AppiumUtils(driver));
    }

    @Test
    @Order(0)
    void search() {
        try {
            LoggerUtil.info("Starting search test");
            homePage.search(CHANNEL_NAME);
            LoggerUtil.info("Search test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during search: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void clickOnChannel() {
        try {
            LoggerUtil.info("Starting click on channel test");
            searchResultsPage.clickOnChannel();
            LoggerUtil.info("Click on channel test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during click on channel: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void clickOnFirstVideo() {
        try {
            LoggerUtil.info("Starting click on first video test");
            channelPage.clickOnVideosTab();
            Assertions.assertTrue(channelPage.clickOnFirstVideo(),
                    "Failed to navigate to video playback screen after clicking video.");
            LoggerUtil.info("Click on first video test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during click on first video: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    void tearDown() {
        LoggerUtil.info("Shutting down all emulators and appium servers");
        emulatorManager.killAllEmulators();
        appiumServerManager.killAllAppiumServers();
    }

}

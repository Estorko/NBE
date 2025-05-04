package com.nbe.automation.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.nbe.automation.core.utils.LoggerUtil;
import com.nbe.automation.pages.Youtube.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ComponentScan(basePackages = "com.nbe.automation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class YoutubeTest {
      
    @Autowired
    private ChannelPage channelPage;

    @Autowired
    private HomePage homePage;

    @Autowired
    private SearchResultsPage searchResultsPage;

    private final String CHANNEL_NAME = "NBE";

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
            Assertions.assertTrue(channelPage.clickOnFirstVideo(), "Failed to navigate to video playback screen after clicking video.");
            LoggerUtil.info("Click on first video test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during click on first video: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

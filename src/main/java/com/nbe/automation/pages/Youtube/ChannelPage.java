package com.nbe.automation.pages.Youtube;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;

import com.nbe.automation.base.AppiumConfig;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

@Component
public class ChannelPage {
    private final AppiumUtils appiumUtils;
    private final AndroidDriver driver;

    public ChannelPage(AppiumConfig appiumConfig, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        this.driver = appiumConfig.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickOnVideosTab() {
        try {
            LoggerUtil.info("Clicking on videos tab");
            appiumUtils.waitForElementByText(Locators.YOUTUBE_CHANNEL_VIDEOS_TAB, 10);
            appiumUtils.clickByText(Locators.YOUTUBE_CHANNEL_VIDEOS_TAB);
            LoggerUtil.info("Clicked on videos tab successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error clicking on videos tab: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean clickOnFirstVideo() {
        try {
            LoggerUtil.info("Clicking on first video");
            final WebElement recyclerView = appiumUtils.findById(Locators.YOUTUBE_SEARCH_RESULT_RECYCLER_VIEW);
            final List<WebElement> videos = recyclerView.findElements(AppiumBy.className("android.view.ViewGroup"));
            videos.get(Locators.YOUTUBE_CHANNEL_VIDEO_VIEW_INDEX).click();
            final boolean isVideoVisible = appiumUtils.waitForElementById(Locators.YOUTUBE_VIDEO_PLAY_VIEW_ID, 10);
            LoggerUtil.info(isVideoVisible
                ? "Video playback screen is visible after clicking the video."
                : "Video playback screen is NOT visible after clicking the video.");
            return isVideoVisible;
        } catch (Exception e) {
            LoggerUtil.error("Error clicking on first video: " + e.getMessage(), e);
            return false;
        }
    }
    
    
}

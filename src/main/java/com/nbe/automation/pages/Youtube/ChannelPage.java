package com.nbe.automation.pages.Youtube;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class ChannelPage {
    private final AppiumUtils appiumUtils;

    public ChannelPage(AndroidDriver driver, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickOnVideosTab() {
        try {
            LoggerUtil.info("Clicking on videos tab", this.getClass());
            appiumUtils.scrollToElementByText(Locators.YOUTUBE_CHANNEL_VIDEOS_TAB);
            // appiumUtils.waitForElementByText(Locators.YOUTUBE_CHANNEL_VIDEOS_TAB, 10);
            appiumUtils.clickByText(Locators.YOUTUBE_CHANNEL_VIDEOS_TAB);
            LoggerUtil.info("Clicked on videos tab successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error clicking on videos tab: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    public boolean clickOnFirstVideo() {
        try {
            LoggerUtil.debug("Clicking on first video with content-desc containing '- play video'", this.getClass());
            appiumUtils.scrollDown();
            Thread.sleep(1000);
            final WebElement recyclerView = appiumUtils.findById(Locators.YOUTUBE_SEARCH_RESULT_RECYCLER_VIEW);
            WebElement playableVideo = appiumUtils.findByContentDescContainingWithXPath(recyclerView,
                    Locators.YOUTUBE_CHANNEL_VIDEO_VIEW_CONTENT_DESC);
            if (playableVideo.equals(null)) {
                LoggerUtil.warn("No video element with matching content-desc were found.", this.getClass());
                return false;
            }
            LoggerUtil
                    .info("Clicking on element with content-desc: " + playableVideo.getAttribute("content-desc"),
                            this.getClass());
            playableVideo.click();
            boolean isVideoVisible = appiumUtils.waitForElementById(Locators.YOUTUBE_VIDEO_PLAY_VIEW_ID, 10);
            LoggerUtil.info(isVideoVisible
                    ? "Video playback screen is visible after clicking."
                    : "Video playback screen is NOT visible after clicking.", this.getClass());
            return isVideoVisible;

        } catch (Exception e) {
            LoggerUtil.error("Error clicking on first playable video: " + e.getMessage(), e);
            return false;
        }
    }

}

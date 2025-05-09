package com.nbe.automation.pages.youtube;

import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ChannelPage extends BasePage {

    private final By homeButton = AppiumBy.androidUIAutomator("new UiSelector().description(\"Home\").instance(1)");
    private final By videosTab = AppiumBy
            .androidUIAutomator("new UiSelector().textMatches(\"(?i)^" + Pattern.quote("Videos") + "$\")");
    private final By videoPlayView = AppiumBy.id("com.google.android.youtube:id/watch_while_time_bar_view_overlay");
    private final By videoPartialContentDesc = AppiumBy.accessibilityId("- play video");
    private final By searchResultRecyclerView = AppiumBy.id("com.google.android.youtube:id/results");

    public ChannelPage(AndroidDriver driver) {
        super(driver);
    }

    public void clickOnVideosTab() {
        try {
            LoggerUtil.info("Clicking on videos tab", this.getClass());
            find(videosTab).click();
            LoggerUtil.info("Clicked on videos tab successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error clicking on videos tab: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    public boolean clickOnFirstVideo() {
        try {
            LoggerUtil.debug("Clicking on first video with content-desc containing '- play video'", this.getClass());
            scrollDown();
            Thread.sleep(1000);
            final WebElement recyclerView = find(searchResultRecyclerView);
            WebElement playableVideo = findByPartialContentDesc(videoPartialContentDesc, recyclerView);
            if (playableVideo == null) {
                LoggerUtil.warn("No video element with matching content-desc were found.", this.getClass());
                return false;
            }
            LoggerUtil
                    .info("Clicking on element with content-desc: " + playableVideo.getAttribute("content-desc"),
                            this.getClass());
            playableVideo.click();
            boolean isVideoVisible = waitForElement(videoPlayView, 10);
            LoggerUtil.info(isVideoVisible
                    ? "Video playback screen is visible after clicking."
                    : "Video playback screen is NOT visible after clicking.", this.getClass());
            return isVideoVisible;

        } catch (Exception e) {
            LoggerUtil.error("Error clicking on first playable video: " + e.getMessage(), e, this.getClass());
            return false;
        }
    }

    public void goBackToHomePage() {
        try {
            find(homeButton).click();
        } catch (Exception e) {
            LoggerUtil.error("Error going back to HomePage: " + e.getMessage(), e, this.getClass());
        }
    }
}

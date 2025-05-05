package com.nbe.automation.pages.Youtube;

import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SearchResultsPage {

    private final AppiumUtils appiumUtils;

    public SearchResultsPage(AndroidDriver driver, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickOnChannel() {
        try {
            LoggerUtil.info("Clicking on 'View Channel' button");
            appiumUtils.scrollToElementByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON);
            appiumUtils.waitForElementByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON, 5);
            appiumUtils.clickByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON);
            LoggerUtil.info("Clicked on 'View Channel' button successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error clicking on 'View Channel' button: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

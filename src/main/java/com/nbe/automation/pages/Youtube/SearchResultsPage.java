package com.nbe.automation.pages.youtube;

import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.Locators;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SearchResultsPage {

    private final BasePage appiumUtils;

    public SearchResultsPage(AndroidDriver driver, BasePage appiumUtils) {
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickOnChannel() {
        try {
            LoggerUtil.info("Clicking on 'View Channel' button", this.getClass());
            appiumUtils.scrollToElementByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON);
            // appiumUtils.waitForElementByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON,
            // 5);
            appiumUtils.clickByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON);
            LoggerUtil.info("Clicked on 'View Channel' button successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error clicking on 'View Channel' button: %s", e.getMessage()), e,
                    this.getClass());
            throw new RuntimeException(e);
        }
    }
}

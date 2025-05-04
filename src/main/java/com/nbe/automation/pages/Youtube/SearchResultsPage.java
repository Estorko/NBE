package com.nbe.automation.pages.Youtube;

import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;

import com.nbe.automation.base.AppiumConfig;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

@Component
public class SearchResultsPage {

    private final AppiumUtils appiumUtils;
    private final AndroidDriver driver;

    public SearchResultsPage(AppiumConfig appiumConfig, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        this.driver = appiumConfig.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickOnChannel() {
        try {
            LoggerUtil.info("Clicking on 'View Channel' button");
            appiumUtils.waitForElementByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON, 10);
            appiumUtils.clickByAccessibilityId(Locators.YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON);
            LoggerUtil.info("Clicked on 'View Channel' button successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error clicking on 'View Channel' button: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

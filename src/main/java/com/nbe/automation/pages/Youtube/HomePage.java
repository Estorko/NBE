package com.nbe.automation.pages.Youtube;

import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class HomePage {
    private final AppiumUtils appiumUtils;
    private final AndroidDriver driver;

    public HomePage(AndroidDriver driver, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        this.driver = driver;  // Inject the driver that is passed from the test class
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void search(String searchQuery) {
        try {
            LoggerUtil.info("Starting search process");
            appiumUtils.waitForElementByAccessibilityId(Locators.YOUTUBE_SEARCH_BUTTON, 10);
            appiumUtils.clickByAccessibilityId(Locators.YOUTUBE_SEARCH_BUTTON);
            appiumUtils.clickById(Locators.YOUTUBE_SEARCH_FIELD_ID);
            appiumUtils.sendKeysById(Locators.YOUTUBE_SEARCH_FIELD_ID, searchQuery);
            driver.pressKey(new KeyEvent(AndroidKey.ENTER));
            LoggerUtil.info("Search process completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during search: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

package com.nbe.automation.pages.youtube;

import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.Locators;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;

public class HomePage extends BasePage{
    private final BasePage appiumUtils;
    private final AndroidDriver driver;

    private final By searchButton = AppiumBy.accessibilityId("Search");
    private final By searchField = AppiumBy.id("com.google.android.youtube:id/search_edit_text");

    public HomePage(AndroidDriver driver, BasePage appiumUtils) {
        super(driver);
        this.appiumUtils = appiumUtils;
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void search(String searchQuery) {
        try {
            LoggerUtil.info("Starting search process", this.getClass());
            appiumUtils.waitForElementByAccessibilityId(Locators.YOUTUBE_SEARCH_BUTTON, 10);
            // appiumUtils.clickByAccessibilityId(Locators.YOUTUBE_SEARCH_BUTTON);
            // searchButton.click()
            find(searchButton).click();
            find(searchField).click();
            // appiumUtils.clickById(Locators.YOUTUBE_SEARCH_FIELD_ID);
            // appiumUtils.sendKeysById(Locators.YOUTUBE_SEARCH_FIELD_ID, searchQuery);
            driver.pressKey(new KeyEvent(AndroidKey.ENTER));
            LoggerUtil.info("Search process completed successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error("Error during search: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

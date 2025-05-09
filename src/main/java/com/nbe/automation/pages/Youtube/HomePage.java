package com.nbe.automation.pages.youtube;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final By searchButton = AppiumBy.accessibilityId("Search");
    private final By searchField = AppiumBy.id("com.google.android.youtube:id/search_edit_text");

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    public void search(String searchQuery) {
        try {
            LoggerUtil.info("Starting search process", this.getClass());
            find(searchButton).click();
            find(searchField).click();
            set(searchField, searchQuery);
            enter();
            LoggerUtil.info("Search process completed successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error("Error during search: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

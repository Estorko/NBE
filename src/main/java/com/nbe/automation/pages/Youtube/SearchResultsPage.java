package com.nbe.automation.pages.youtube;

import org.openqa.selenium.By;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class SearchResultsPage extends BasePage {

    private final By viewChannel = AppiumBy.accessibilityId("View Channel");

    public SearchResultsPage(AndroidDriver driver) {
        super(driver);
    }

    public void clickOnChannel() {
        try {
            LoggerUtil.info("Clicking on 'View Channel' button", this.getClass());
            scrollToElement(viewChannel);
            find(viewChannel).click();
            LoggerUtil.info("Clicked on 'View Channel' button successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error clicking on 'View Channel' button: %s", e.getMessage()), e,
                    this.getClass());
            throw new RuntimeException(e);
        }
    }
}

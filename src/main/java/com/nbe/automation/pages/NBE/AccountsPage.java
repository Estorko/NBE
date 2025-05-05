package com.nbe.automation.pages.NBE;

import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AccountsPage {

    private final AppiumUtils appiumUtils;

    public AccountsPage(AndroidDriver driver, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void navigateToAccountDetails() {
        try {
            LoggerUtil.info("Navigating to account details");
            appiumUtils.waitForElementByText(Locators.NBE_ACCOUNTS_TEXT, 20);
            appiumUtils.clickByText(Locators.NBE_ACCOUNTS_TEXT);
            appiumUtils.clickById(Locators.NBE_ACCOUNT_DROPDOWN);
            appiumUtils.clickByText(Locators.NBE_ACCOUNT_DETAILS_TEXT);
            LoggerUtil.info("Successfully navigated to account details");
        } catch (Exception e) {
            LoggerUtil.error("Error navigating to account details: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

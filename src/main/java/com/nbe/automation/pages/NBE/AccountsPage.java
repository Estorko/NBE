package com.nbe.automation.pages.NBE;

import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.Locators;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AccountsPage {

    private final BasePage appiumUtils;

    public AccountsPage(AndroidDriver driver, BasePage appiumUtils) {
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void navigateToAccountDetails() {
        try {
            LoggerUtil.info("Navigating to account details", this.getClass());
            appiumUtils.waitForElementByText(Locators.NBE_ACCOUNTS_TEXT, 20);
            appiumUtils.clickByText(Locators.NBE_ACCOUNTS_TEXT);
            appiumUtils.clickById(Locators.NBE_ACCOUNT_DROPDOWN);
            appiumUtils.clickByText(Locators.NBE_ACCOUNT_DETAILS_TEXT);
            LoggerUtil.info("Successfully navigated to account details", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error("Error navigating to account details: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

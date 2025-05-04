package com.nbe.automation.pages.NBE;

import com.nbe.automation.base.AppiumConfig;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

@Component
public class AccountsPage {

    private final AppiumUtils appiumUtils;
    private final AndroidDriver driver;

    public AccountsPage(AppiumConfig appiumConfig, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        this.driver = appiumConfig.getDriver();
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

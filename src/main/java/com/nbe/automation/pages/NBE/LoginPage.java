package com.nbe.automation.pages.NBE;

import com.nbe.automation.config.AppProperties;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;


public class LoginPage {

    private final AppiumUtils appiumUtils;
    private final AppProperties appProperties;

    public LoginPage(AndroidDriver driver, AppProperties appProperties, AppiumUtils appiumUtils) {
        this.appProperties = appProperties;
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void login() {
        try {
            LoggerUtil.info("Starting login process");
            enterUserId(appProperties.getNbeUserId());
            if (isPasswordScreenVisible()) {
                enterPassword(appProperties.getNbeUserPassword());
                LoggerUtil.info("Login process completed successfully");
            } else {
                LoggerUtil.error("Login failed - Password screen not visible");
                throw new RuntimeException("Login failed - Password screen not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error during login: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void enterUserId(String userId) {
        try {
            LoggerUtil.info("Entering user ID");
            appiumUtils.sendKeysById(Locators.NBE_USER_ID_FIELD, userId);
            appiumUtils.clickByText(Locators.NBE_LOGIN_BUTTON);
            LoggerUtil.info("User ID entered successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error entering user ID: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void enterPassword(String password) {
        try {
            LoggerUtil.info("Entering password");
            appiumUtils.sendKeysById(Locators.NBE_PASSWORD_FIELD, password);
            appiumUtils.waitForElementByText(Locators.NBE_LOGIN_BUTTON, 10);
            if (appiumUtils.isDisplayedByText(Locators.NBE_LOGIN_BUTTON)) {
                appiumUtils.clickByText(Locators.NBE_LOGIN_BUTTON);
                // Wait for Accounts to be visible
                appiumUtils.waitForElementByText(Locators.NBE_ACCOUNTS_TEXT, 10);
                if (appiumUtils.isDisplayedByText(Locators.NBE_ACCOUNTS_TEXT)) {
                    LoggerUtil.info("Password entered successfully and Accounts screen is visible");
                } else {
                    LoggerUtil.error("Accounts screen not visible after login");
                    throw new RuntimeException("Accounts screen not visible after login");
                }
            } else {
                LoggerUtil.error("Login button not visible");
                throw new RuntimeException("Login button not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error entering password: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean isPasswordScreenVisible() {
        try {
            LoggerUtil.debug("Checking if password screen is visible");
            return appiumUtils.isDisplayedById(Locators.NBE_PASSWORD_FIELD);
        } catch (Exception e) {
            LoggerUtil.error("Error checking password screen visibility: " + e.getMessage(), e);
            return false;
        }
    }
}
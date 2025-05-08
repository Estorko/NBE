package com.nbe.automation.pages.NBE;

import org.openqa.selenium.support.PageFactory;

import com.nbe.automation.base.BasePage;
import com.nbe.automation.utils.AutomationUtils;
import com.nbe.automation.utils.Locators;
import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AccountDetailsPage {

    private AutomationUtils automationUtils;

    private final BasePage appiumUtils;

    public AccountDetailsPage(AndroidDriver driver, BasePage appiumUtils) {
        this.appiumUtils = appiumUtils;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public String getAccountNumber() {
        try {
            LoggerUtil.info("Getting account number", this.getClass());
            appiumUtils.waitForElementByText(Locators.NBE_ACCOUNT_NUMBER_TEXT, 20);
            if (appiumUtils.isDisplayedByText(Locators.NBE_ACCOUNT_NUMBER_TEXT)) {
                String accountNumber = appiumUtils.getTextFromChildIndex(
                        Locators.NBE_ACCOUNT_NUMBER_VIEW,
                        Locators.NBE_ACCOUNT_NUMBER_PARENT_INDEX,
                        Locators.NBE_ACCOUNT_NUMBER_CHILD_INDEX);

                if (accountNumber != null && !accountNumber.isEmpty()) {
                    LoggerUtil.info("Successfully retrieved account number", this.getClass());
                    return accountNumber;
                } else {
                    LoggerUtil.error("Failed to get account number", this.getClass());
                    throw new RuntimeException("Failed to get account number - Account number is empty");
                }
            } else {
                LoggerUtil.error("Account number not visible", this.getClass());
                throw new RuntimeException("Account number not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error getting account number: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    public String getIban() {
        try {
            LoggerUtil.info("Getting IBAN", this.getClass());
            appiumUtils.waitForElementByText(Locators.NBE_ACCOUNT_NUMBER_TEXT, 20);
            if (appiumUtils.isDisplayedByText(Locators.NBE_ACCOUNT_NUMBER_TEXT)) {
                String iban = appiumUtils.getTextFromChildIndex(
                        Locators.NBE_ACCOUNT_NUMBER_VIEW,
                        Locators.NBE_IBAN_PARENT_INDEX,
                        Locators.NBE_IBAN_CHILD_INDEX);

                if (iban != null && !iban.isEmpty()) {
                    LoggerUtil.info("Successfully retrieved IBAN", this.getClass());
                    return iban;
                } else {
                    LoggerUtil.error("Failed to get IBAN", this.getClass());
                    throw new RuntimeException("Failed to get IBAN - IBAN is empty");
                }
            } else {
                LoggerUtil.error("IBAN not visible", this.getClass());
                throw new RuntimeException("IBAN not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error getting IBAN: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void scrollDown() {
        try {
            LoggerUtil.info("Scrolling down", this.getClass());
            appiumUtils.scrollDown();
            LoggerUtil.info("Successfully scrolled down", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error scrolling down: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    public void scrollUp() {
        try {
            LoggerUtil.info("Scrolling up", this.getClass());
            appiumUtils.scrollUp();
            LoggerUtil.info("Successfully scrolled up", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error("Error scrolling up: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void logOut() {
        int attempts = 0;
        boolean logoutSuccess = false;

        while (attempts < 3 && !logoutSuccess) {
            try {
                LoggerUtil.info("Attempting to log out (try " + (attempts + 1) + ")", this.getClass());
                appiumUtils.waitForElementByAccessibilityId(Locators.NBE_LOGOUT_INDEX_BUTTON, 5);
                automationUtils.clickUntilVisible(Locators.NBE_LOGOUT_INDEX_BUTTON, Locators.NBE_LOGIN_BUTTON, 3);
                appiumUtils.waitForElementByText(Locators.NBE_LOGIN_BUTTON, 5);
                if (appiumUtils.isDisplayedByText(Locators.NBE_LOGIN_BUTTON)) {
                    logoutSuccess = true;
                    LoggerUtil.info("Logout completed successfully", this.getClass());
                    break;
                } else {
                    LoggerUtil.warn("Logout click succeeded but login screen not found", this.getClass());
                }
            } catch (Exception e) {
                LoggerUtil.warn(String.format("Logout attempt %s failed: %s", (attempts + 1), e.getMessage()),
                        this.getClass());
            }

            attempts++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        if (!logoutSuccess) {
            LoggerUtil.error("Logout failed after 3 attempts", this.getClass());
            throw new RuntimeException("Logout failed - Login screen not visible");
        }
    }
}

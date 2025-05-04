package com.nbe.automation.pages.NBE;

import com.nbe.automation.base.AppiumConfig;
import com.nbe.automation.core.utils.AppiumUtils;
import com.nbe.automation.core.utils.AutomationUtils;
import com.nbe.automation.core.utils.Locators;
import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsPage {

    @Autowired
    private AutomationUtils automationUtils;
    
    private final AppiumUtils appiumUtils;
    private final AndroidDriver driver;

    public AccountDetailsPage(AppiumConfig appiumConfig, AppiumUtils appiumUtils) {
        this.appiumUtils = appiumUtils;
        this.driver = appiumConfig.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public String getAccountNumber() {
        try {
            LoggerUtil.info("Getting account number");
            appiumUtils.waitForElementByText(Locators.NBE_ACCOUNT_NUMBER_TEXT, 20);
            if (appiumUtils.isDisplayedByText(Locators.NBE_ACCOUNT_NUMBER_TEXT)) {
                String accountNumber = appiumUtils.getTextFromChildIndex(
                        Locators.NBE_ACCOUNT_NUMBER_VIEW,
                        Locators.NBE_ACCOUNT_NUMBER_PARENT_INDEX,
                        Locators.NBE_ACCOUNT_NUMBER_CHILD_INDEX);

                if (accountNumber != null && !accountNumber.isEmpty()) {
                    LoggerUtil.info("Successfully retrieved account number");
                    return accountNumber;
                } else {
                    LoggerUtil.error("Failed to get account number");
                    throw new RuntimeException("Failed to get account number - Account number is empty");
                }
            } else {
                LoggerUtil.error("Account number not visible");
                throw new RuntimeException("Account number not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error getting account number: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String getIban() {
        try {
            LoggerUtil.info("Getting IBAN");
            appiumUtils.waitForElementByText(Locators.NBE_ACCOUNT_NUMBER_TEXT, 20);
            if (appiumUtils.isDisplayedByText(Locators.NBE_ACCOUNT_NUMBER_TEXT)) {
                String iban = appiumUtils.getTextFromChildIndex(
                        Locators.NBE_ACCOUNT_NUMBER_VIEW,
                        Locators.NBE_IBAN_PARENT_INDEX,
                        Locators.NBE_IBAN_CHILD_INDEX);

                if (iban != null && !iban.isEmpty()) {
                    LoggerUtil.info("Successfully retrieved IBAN");
                    return iban;
                } else {
                    LoggerUtil.error("Failed to get IBAN");
                    throw new RuntimeException("Failed to get IBAN - IBAN is empty");
                }
            } else {
                LoggerUtil.error("IBAN not visible");
                throw new RuntimeException("IBAN not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error getting IBAN: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void scrollDown() {
        try {
            LoggerUtil.info("Scrolling down");
            appiumUtils.scrollDown();
            LoggerUtil.info("Successfully scrolled down");
        } catch (Exception e) {
            LoggerUtil.error("Error scrolling down: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void scrollUp() {
        try {
            LoggerUtil.info("Scrolling up");
            appiumUtils.scrollUp();
            LoggerUtil.info("Successfully scrolled up");
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
                LoggerUtil.info("Attempting to log out (try " + (attempts + 1) + ")");
                appiumUtils.waitForElementByAccessibilityId(Locators.NBE_LOGOUT_INDEX_BUTTON, 5);
                automationUtils.clickUntilVisible(Locators.NBE_LOGOUT_INDEX_BUTTON, Locators.NBE_LOGIN_BUTTON, 3);
                appiumUtils.waitForElementByText(Locators.NBE_LOGIN_BUTTON, 5);
                if (appiumUtils.isDisplayedByText(Locators.NBE_LOGIN_BUTTON)) {
                    logoutSuccess = true;
                    LoggerUtil.info("Logout completed successfully");
                    break;
                } else {
                    LoggerUtil.warn("Logout click succeeded but login screen not found");
                }
            } catch (Exception e) {
                LoggerUtil.warn("Logout attempt " + (attempts + 1) + " failed: " + e.getMessage());
            }
    
            attempts++;
            try {
                Thread.sleep(1000); // Small delay between retries
            } catch (InterruptedException ignored) {
            }
        }
    
        if (!logoutSuccess) {
            LoggerUtil.error("Logout failed after 3 attempts");
            throw new RuntimeException("Logout failed - Login screen not visible");
        }
    }
}

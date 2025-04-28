package com.nbe.automation;

import com.nbe.automation.base.TestEnvironment;
import com.nbe.automation.utils.AppiumUtils;
import com.nbe.automation.utils.ConfigReader;
import com.nbe.automation.utils.LoggerUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NBETest extends TestEnvironment {
    private AppiumUtils appiumUtils;

    @BeforeClass
    public void beforeClass() {
        ConfigReader.loadProperties();
    }

    @BeforeMethod
    public void setup() {
        appiumUtils = new AppiumUtils(driver);
        LoggerUtil.info("AppiumUtils initialized");
    }

    @Test(priority = 1, description = "User can Login with valid credentials - user id and password")
    public void login() {
        try {
            LoggerUtil.info("Starting login test");
            enterUserId();
            if (appiumUtils.isDisplayedByText("Forgot your Password?")) {
                enterPassword();
                LoggerUtil.info("Login test completed successfully");
            } else {
                LoggerUtil.error("Login test failed - Password screen not visible");
                throw new RuntimeException("Login test failed - Password screen not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error during login: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 2, description = "User selects the account and opens the Account Overview")
    public void selectAccount() {
        try {
            LoggerUtil.info("Selecting account");
            appiumUtils.clickByText("Accounts");
            appiumUtils.clickById("CSA0");
            appiumUtils.clickByText("Account Details");
        } catch (Exception e) {
            LoggerUtil.error("Error selecting account: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 3, description = "Get the Account Number")
    public void getAccountNumber() {
        try {
            LoggerUtil.info("Getting account number");
            String accountNumber = appiumUtils.getTextFromChildIndex("android.view.View", 14, 3);
            String iban = appiumUtils.getTextFromChildIndex("android.view.View", 18, 3);
            if (accountNumber != null && !accountNumber.isEmpty() && iban != null && !iban.isEmpty()) {
                String maskedAccountNumber = maskAccountNumber(accountNumber);
                String maskedIban = maskAccountNumber(iban);
                LoggerUtil.info("Masked account number: " + maskedAccountNumber);
                LoggerUtil.info("Masked IBAN: " + maskedIban);
            } else {
                LoggerUtil.error("Failed to get account details");
                throw new RuntimeException("Failed to get account details - Account number or IBAN is empty");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error getting account number: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 4, description = "Verify scroll functionality")
    public void verifyScroll() {
        try {
            LoggerUtil.info("Starting scroll test");
            appiumUtils.scrollDown();
            LoggerUtil.info("Scroll down completed");
            Thread.sleep(1000);

            appiumUtils.scrollUp();
            LoggerUtil.info("Scroll up completed");
            Thread.sleep(1000);
            LoggerUtil.info("Scroll test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during scroll test: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 5, description = "Logout from the application")
    public void logOut() {
        try {
            LoggerUtil.info("Logging out");
            int attempts = 0;
            boolean logoutSuccess = false;
            
            while (attempts < 3 && !logoutSuccess) {
                try {
                    appiumUtils.clickByAccessibilityId("index");
                    Thread.sleep(1000);
                    if (appiumUtils.isDisplayedByText("Login")) {
                        logoutSuccess = true;
                        LoggerUtil.info("Logout completed successfully");
                    } else {
                        attempts++;
                        LoggerUtil.error("Logout attempt " + attempts + " failed - Login screen not visible");
                    }
                } catch (Exception e) {
                    attempts++;
                    LoggerUtil.error("Logout attempt " + attempts + " failed: " + e.getMessage());
                }
            }
            
            if (!logoutSuccess) {
                throw new RuntimeException("Logout failed after 3 attempts - Login screen not visible");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error during logout: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String maskAccountNumber(String accountNumber) {
        char[] accountChars = accountNumber.toCharArray();
        List<Integer> digitIndices = getDigitIndices(accountChars);
        Collections.shuffle(digitIndices);

        int digitsToMask = digitIndices.size() / 2;
        for (int i = 0; i < digitsToMask; i++) {
            accountChars[digitIndices.get(i)] = 'x';
        }

        return new String(accountChars);
    }

    private List<Integer> getDigitIndices(char[] chars) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                indices.add(i);
            }
        }
        return indices;
    }

    public void enterUserId() {
        try {
            LoggerUtil.info("Entering user ID");
            String userId = ConfigReader.getProperty("user.id");
            appiumUtils.sendKeysById("login_username", userId);
            appiumUtils.clickByText("Login");
            LoggerUtil.info("User ID entered successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error entering user id: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void enterPassword() {
        try {
            LoggerUtil.info("Entering password");
            String password = ConfigReader.getProperty("user.password");
            appiumUtils.sendKeysById("login_password", password);
            if (appiumUtils.isDisplayedByText("Login")) {
                appiumUtils.clickByText("Login");
                // Wait for Accounts to be visible
                int attempts = 0;
                while (!appiumUtils.isDisplayedByText("Accounts") && attempts < 10) {
                    Thread.sleep(1000);
                    attempts++;
                }
                if (appiumUtils.isDisplayedByText("Accounts")) {
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
}
package com.nbe.automation;

import com.nbe.automation.base.TestEnvironment;
import com.nbe.automation.utils.AppiumUtils;
import com.nbe.automation.utils.ConfigReader;
import com.nbe.automation.utils.LoggerUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NBETest extends TestEnvironment {
    private AppiumUtils appiumUtils;

    @BeforeMethod
    public void setup() {
        appiumUtils = new AppiumUtils(driver);
        ConfigReader.loadProperties();
        LoggerUtil.info("AppiumUtils initialized");
    }

    @Test(priority = 1, description = "User can Login with valid credentials - user id and password")
    public void login() {
        try {
            LoggerUtil.info("Starting login test");
            enterUserId();
            if(appiumUtils.isDisplayedByText("Forgot your Password?")) {
                // enterPassword();
                LoggerUtil.info("Login test completed successfully");
            }
            else {
                LoggerUtil.info("Login test failed");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error during login: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    // @Test (priority = 2, description = "User selects the account and opens the Account Overview")
    // public void selectAccount() {
    //     try {
    //         LoggerUtil.info("Selecting account");
    //         appiumUtils.clickByText("Accounts");
    //         appiumUtils.clickByText("Account Overview");
    //         LoggerUtil.info("Account selected successfully");
    //     } catch (Exception e) {
    //         LoggerUtil.error("Error selecting account: " + e.getMessage(), e);
    //         throw new RuntimeException(e);
    //     }
        
    // }

    public void enterUserId() {
        try {
            LoggerUtil.info("Entering user ID");
            String userId = ConfigReader.getProperty("user.id");
            appiumUtils.sendKeysByXPath("//android.widget.EditText[@resource-id='login_username']", userId);
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
            appiumUtils.sendKeysByXPath("//android.widget.EditText[@resource-id='login_password']", password);
            appiumUtils.clickByText("Login");
            LoggerUtil.info("Password entered successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error entering password: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
package com.nbe.automation;

import com.nbe.automation.base.BaseTest;
import com.nbe.automation.utils.AppiumUtils;
import org.testng.annotations.Test;

public class NBETest extends BaseTest {
    private final AppiumUtils appiumUtils;

    public NBETest() {
        this.appiumUtils = new AppiumUtils(driver);
    }

    @Test
    public void testLogin() {
        try {
            // Example login flow
            appiumUtils.sendKeys("usernameField", "your_username");
            appiumUtils.sendKeys("passwordField", "your_password");
            appiumUtils.clickElement("loginButton");
            
            // Verify login success
            appiumUtils.isElementDisplayed("dashboard");
            
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAccountBalance() {
        try {
            // Navigate to account balance
            appiumUtils.clickElement("accountsMenu");
            appiumUtils.clickElement("balanceButton");
            
            // Verify balance is displayed
            String balance = appiumUtils.getText("balanceAmount");
            System.out.println("Account Balance: " + balance);
            
        } catch (Exception e) {
            System.out.println("Error checking balance: " + e.getMessage());
            throw e;
        }
    }
} 
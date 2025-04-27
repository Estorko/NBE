package com.nbe.automation;

import com.nbe.automation.base.BaseTest;
import com.nbe.automation.utils.AppiumUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
            WebElement button1 = driver.findElement(By.id("com.google.android.youtube:id/error_retry_button"));
            button1.click();   
        } catch (Exception e) {
            System.out.println("Error checking balance: " + e.getMessage());
            throw e;
        }
    }
} 
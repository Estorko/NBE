package com.nbe.automation.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AppiumUtils {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public AppiumUtils(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WebElement waitForElement(String accessibilityId) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId(accessibilityId)));
    }

    public void clickElement(String accessibilityId) {
        waitForElement(accessibilityId).click();
    }

    public void sendKeys(String accessibilityId, String text) {
        WebElement element = waitForElement(accessibilityId);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(String accessibilityId) {
        return waitForElement(accessibilityId).getText();
    }

    public boolean isElementDisplayed(String accessibilityId) {
        try {
            return waitForElement(accessibilityId).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
} 
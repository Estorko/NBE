package com.nbe.automation.utils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

public class AppiumUtils {
    private final AndroidDriver<MobileElement> driver;

    public AppiumUtils(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
    }

    // Find by ID
    public WebElement findById(String id) {
        LoggerUtil.debug("Finding element by ID: " + id);
        return driver.findElement(By.id(id));
    }

    // Find by Accessibility ID ("content-desc")
    public WebElement findByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Finding element by Accessibility ID: " + accessibilityId);
        return driver.findElement(By.xpath("//*[@content-desc='" + accessibilityId + "']"));
    }

    // Find by XPath
    public WebElement findByXPath(String xpath) {
        LoggerUtil.debug("Finding element by XPath: " + xpath);
        return driver.findElement(By.xpath(xpath));
    }

    // Find by Text
    public WebElement findByText(String text) {
        LoggerUtil.debug("Finding element by text: " + text);
        return driver.findElement(By.xpath("//*[@text='" + text + "']"));
    }

    public void clickById(String id) {
        LoggerUtil.debug("Clicking element by ID: " + id);
        driver.findElement(By.id(id)).click();
    }

    public void clickByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Clicking element by Accessibility ID: " + accessibilityId);
        driver.findElement(By.xpath("//*[@content-desc='" + accessibilityId + "']")).click();
    }

    public void clickByXPath(String xpath) {
        LoggerUtil.debug("Clicking element by XPath: " + xpath);
        driver.findElement(By.xpath(xpath)).click();
    }

    public void clickByText(String text) {
        LoggerUtil.debug("Clicking element by text: " + text);
        findByText(text).click();
    }

    // Send keys methods (Enter text)
    public void sendKeysById(String id, String text) {
        LoggerUtil.debug("Sending keys to element by ID: " + id);
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysByAccessibilityId(String accessibilityId, String text) {
        LoggerUtil.debug("Sending keys to element by Accessibility ID: " + accessibilityId);
        WebElement element = driver.findElement(By.xpath("//*[@content-desc='" + accessibilityId + "']"));
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysByText(String text, String value) {
        LoggerUtil.debug("Sending keys to element by text: " + text);
        WebElement element = findByText(text);
        element.clear();
        element.sendKeys(value);
    }

    public void sendKeysByClass(String className, String text, int index) {
        LoggerUtil.debug("Sending keys to element by class: " + className + " at index: " + index);
        List<MobileElement> elements = driver.findElements(By.className(className));
        if (!elements.isEmpty()) {
            elements.get(index).clear();
            elements.get(index).sendKeys(text);
        } else {
            LoggerUtil.warn("No elements found with class: " + className);
        }
    }

    public void sendKeysByXPath(String xpath, String text) {
        try {
            LoggerUtil.debug("Sending keys to element by XPath: " + xpath);
            WebElement element = driver.findElement(By.xpath(xpath));
            element.clear();
            element.sendKeys(text);
            LoggerUtil.debug("Successfully sent keys to element");
        } catch (Exception e) {
            LoggerUtil.error("Error sending keys to element by XPath: " + e.getMessage(), e);
            throw new RuntimeException("Failed to send keys to element by XPath: " + xpath, e);
        }
    }

    // Get text methods
    public String getTextById(String id) {
        LoggerUtil.debug("Getting text from element by ID: " + id);
        return driver.findElement(By.id(id)).getText();
    }

    public String getTextByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Getting text from element by Accessibility ID: " + accessibilityId);
        return driver.findElement(By.xpath("//*[@content-desc='" + accessibilityId + "']")).getText();
    }

    // Check visibility methods
    public boolean isDisplayedById(String id) {
        try {
            LoggerUtil.debug("Checking visibility of element by ID: " + id);
            return driver.findElement(By.id(id)).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.warn("Element not found by ID: " + id);
            return false;
        }
    }

    public boolean isDisplayedByAccessibilityId(String accessibilityId) {
        try {
            LoggerUtil.debug("Checking visibility of element by Accessibility ID: " + accessibilityId);
            return driver.findElement(By.xpath("//*[@content-desc='" + accessibilityId + "']")).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.warn("Element not found by Accessibility ID: " + accessibilityId);
            return false;
        }
    }

    public boolean isDisplayedByText(String text) {
        try {
            LoggerUtil.debug("Checking visibility of element by text: " + text);
            return findByText(text).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.warn("Element not found by text: " + text);
            return false;
        }
    }
}
package com.nbe.automation.utils;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class AppiumUtils {
    private final AndroidDriver<MobileElement> driver;

    public AppiumUtils(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
    }

    public WebElement findById(String id) {
        LoggerUtil.debug("Finding element by ID: " + id);
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + id + "\")"));
    }

    public WebElement findByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Finding element by Accessibility ID: " + accessibilityId);
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")"));
    }

    public WebElement findByXPath(String xpath) {
        LoggerUtil.debug("Finding element by XPath: " + xpath);
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().xpath(\"" + xpath + "\")"));
    }

    public WebElement findByText(String text) {
        LoggerUtil.debug("Finding element by text: " + text);
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")"));
    }

    public void clickById(String id) {
        LoggerUtil.debug("Clicking element by ID: " + id);
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + id + "\")")).click();
    }

    public void clickByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Clicking element by Accessibility ID: " + accessibilityId);
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")")).click();
    }

    public void clickByXPath(String xpath) {
        try {
            LoggerUtil.debug("Clicking element by XPath: " + xpath);
            // Extract text from XPath
            String text = xpath.replaceAll(".*@text='([^']*)'.*", "$1");
            driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")")).click();
        } catch (Exception e) {
            LoggerUtil.error("Error clicking element by XPath: " + e.getMessage(), e);
            throw new RuntimeException("Failed to click element by XPath: " + xpath, e);
        }
    }

    public void clickByText(String text) {
        LoggerUtil.debug("Clicking element by text: " + text);
        findByText(text).click();
    }

    public void sendKeysById(String id, String text) {
        LoggerUtil.debug("Sending keys to element by ID: " + id);
        WebElement element = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + id + "\")"));
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysByAccessibilityId(String accessibilityId, String text) {
        LoggerUtil.debug("Sending keys to element by Accessibility ID: " + accessibilityId);
        WebElement element = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")"));
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
        List<MobileElement> elements = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().className(\"" + className + "\")"));
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
            // Extract resource-id from XPath
            String resourceId = xpath.replaceAll(".*@resource-id='([^']*)'.*", "$1");
            WebElement element = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + resourceId + "\")"));
            element.clear();
            element.sendKeys(text);
            LoggerUtil.debug("Successfully sent keys to element");
        } catch (Exception e) {
            LoggerUtil.error("Error sending keys to element by XPath: " + e.getMessage(), e);
            throw new RuntimeException("Failed to send keys to element by XPath: " + xpath, e);
        }
    }

    public String getTextById(String id) {
        LoggerUtil.debug("Getting text from element by ID: " + id);
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + id + "\")")).getText();
    }

    public String getTextByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Getting text from element by Accessibility ID: " + accessibilityId);
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")")).getText();
    }

    public boolean isDisplayedById(String id) {
        try {
            LoggerUtil.debug("Checking visibility of element by ID: " + id);
            return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"" + id + "\")")).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.warn("Element not found by ID: " + id);
            return false;
        }
    }

    public boolean isDisplayedByAccessibilityId(String accessibilityId) {
        try {
            LoggerUtil.debug("Checking visibility of element by Accessibility ID: " + accessibilityId);
            return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")")).isDisplayed();
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

    public String getTextByText(String text) {
        try {
            LoggerUtil.debug("Getting text from element by text: " + text);
            return driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")")).getText();
        } catch (Exception e) {
            LoggerUtil.error("Error getting text from element by text: " + e.getMessage(), e);
            throw new RuntimeException("Failed to get text from element with text: " + text, e);
        }
    }

    public WebElement findByClassNameAndInstance(String className, int instance) {
        try {
            LoggerUtil.debug("Finding element by class name: " + className + " and instance: " + instance);
            return driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().className(\"" + className + "\").instance(" + instance + ")"));
        } catch (Exception e) {
            LoggerUtil.error("Error finding element by class name and instance: " + e.getMessage(), e);
            throw new RuntimeException("Failed to find element with class: " + className + " and instance: " + instance, e);
        }
    }

    public String getTextFromChildIndex(String className, int parentInstance, int childIndex) {
        try {
            LoggerUtil.debug("Getting text from child index " + childIndex + " of element with class: " + className + " and instance: " + parentInstance);
            WebElement parent = findByClassNameAndInstance(className, parentInstance);
            List<MobileElement> children = parent.findElements(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\")"));
            if (children.size() > childIndex) {
                return children.get(childIndex).getText();
            }
            throw new RuntimeException("Child index " + childIndex + " not found");
        } catch (Exception e) {
            LoggerUtil.error("Error getting text from child index: " + e.getMessage(), e);
            throw new RuntimeException("Failed to get text from child index " + childIndex, e);
        }
    }

    public void scrollDown() {
        try {
            LoggerUtil.debug("Starting scroll down");
            // Get screen dimensions
            int screenHeight = driver.manage().window().getSize().getHeight();
            int screenWidth = driver.manage().window().getSize().getWidth();
            
            // Calculate start and end points for scroll
            int startX = screenWidth / 2;
            int startY = (int) (screenHeight * 0.8);
            int endY = (int) (screenHeight * 0.2);
            
            // Create W3C Actions
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Collections.singletonList(scroll));
            LoggerUtil.debug("Scroll down completed");
            
        } catch (Exception e) {
            LoggerUtil.error("Scroll down failed: " + e.getMessage());
            throw new RuntimeException("Failed to scroll down: " + e.getMessage(), e);
        }
    }

    public void scrollUp() {
        try {
            LoggerUtil.debug("Starting scroll up");
            // Get screen dimensions
            int screenHeight = driver.manage().window().getSize().getHeight();
            int screenWidth = driver.manage().window().getSize().getWidth();
            
            // Calculate start and end points for scroll (opposite of scrollDown)
            int startX = screenWidth / 2;
            int startY = (int) (screenHeight * 0.2);
            int endY = (int) (screenHeight * 0.8);
            
            // Create W3C Actions
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Collections.singletonList(scroll));
            LoggerUtil.debug("Scroll up completed");
            
        } catch (Exception e) {
            LoggerUtil.error("Scroll up failed: " + e.getMessage());
            throw new RuntimeException("Failed to scroll up: " + e.getMessage(), e);
        }
    }

    public WebElement findByAccessibilityIdWithinParent(WebElement parent, String accessibilityId) {
        LoggerUtil.debug("Finding element by Accessibility ID within parent: " + accessibilityId);
        return parent.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")"));
    }
}
package com.nbe.automation.core.utils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AppiumUtils {
    private final AndroidDriver driver;

    public AppiumUtils(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public WebElement findById(String id) {
        LoggerUtil.debug("Finding element by ID: " + id);
        return driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"" + id + "\")"));
    }

    public WebElement findByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Finding element by Accessibility ID: " + accessibilityId);
        return driver
                .findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")"));
    }

    public WebElement findByXPath(String xpath) {
        LoggerUtil.debug("Finding element by XPath: " + xpath);
        return driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().xpath(\"" + xpath + "\")"));
    }

    public WebElement findByText(String text) {
        LoggerUtil.debug("Finding element by text: " + text);
        return driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")"));
    }

    public void clickById(String id) {
        LoggerUtil.debug("Clicking element by ID: " + id);
        findById(id).click();
    }

    public void clickByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Clicking element by Accessibility ID: " + accessibilityId);
        findByAccessibilityId(accessibilityId).click();
    }

    // not used
    public void clickByXPath(String xpath) {
        try {
            LoggerUtil.debug("Clicking element by XPath: " + xpath);
            String text = xpath.replaceAll(".*@text='([^']*)'.*", "$1");
            driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")")).click();
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
        WebElement element = findById(id);
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysByAccessibilityId(String accessibilityId, String text) {
        LoggerUtil.debug("Sending keys to element by Accessibility ID: " + accessibilityId);
        WebElement element = findByAccessibilityId(accessibilityId);
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
        List<WebElement> elements = driver
                .findElements(AppiumBy.androidUIAutomator("new UiSelector().className(\"" + className + "\")"));
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
            WebElement element = findById(resourceId);
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
        return findById(id).getText();
    }

    public String getTextByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Getting text from element by Accessibility ID: " + accessibilityId);
        return findByAccessibilityId(accessibilityId).getText();
    }

    public boolean isDisplayedById(String id) {
        try {
            LoggerUtil.debug("Checking visibility of element by ID: " + id);
            return findById(id).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.warn("Element not found by ID: " + id);
            return false;
        }
    }

    public boolean isDisplayedByAccessibilityId(String accessibilityId) {
        try {
            LoggerUtil.debug("Checking visibility of element by Accessibility ID: " + accessibilityId);
            return findByAccessibilityId(accessibilityId).isDisplayed();
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
            return findByText(text).getText();
        } catch (Exception e) {
            LoggerUtil.error("Error getting text from element by text: " + e.getMessage(), e);
            throw new RuntimeException("Failed to get text from element with text: " + text, e);
        }
    }

    public WebElement findByClassNameAndInstance(String className, int instance) {
        try {
            LoggerUtil.debug("Finding element by class name: " + className + " and instance: " + instance);
            return driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"" + className + "\").instance(" + instance + ")"));
        } catch (Exception e) {
            LoggerUtil.error("Error finding element by class name and instance: " + e.getMessage(), e);
            throw new RuntimeException("Failed to find element with class: " + className + " and instance: " + instance,
                    e);
        }
    }

    public String getTextFromChildIndex(String className, int parentInstance, int childIndex) {
        try {
            LoggerUtil.debug("Getting text from child index " + childIndex + " of element with class: " + className
                    + " and instance: " + parentInstance);
            WebElement parent = findByClassNameAndInstance(className, parentInstance);
            List<WebElement> children = parent.findElements(
                    AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.TextView\")"));
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
                    .addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX,
                            endY))
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
                    .addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX,
                            endY))
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
        return parent
                .findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")"));
    }

    public WebElement findByContentDescContainingWithXPath(WebElement parent, String partialContentDesc) {
        LoggerUtil.debug(
                "Finding element within parent by partial (case-insensitive) content-desc: " + partialContentDesc);
        String lowered = partialContentDesc.toLowerCase();
        String xpath = ".//*[@content-desc and contains(translate(@content-desc, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                + lowered + "')]";
        return parent.findElement(AppiumBy.xpath(xpath));
    }

    public void scrollToElementByAccessibilityId(String accessibilityId) {
        LoggerUtil.debug("Scrolling down to element by accessibility ID: " + accessibilityId);
        if (isDisplayedByAccessibilityId(accessibilityId)) {
            LoggerUtil.debug("Element is already visible, no need to scroll");
            return;
        } else {
            try {
                String uiScrollable = "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".setAsVerticalList()"
                        + ".scrollForward()" // Explicitly scroll forward (down)
                        + ".scrollIntoView(new UiSelector().description(\"" + accessibilityId + "\"))";
                driver.findElement(AppiumBy.androidUIAutomator(uiScrollable));
                LoggerUtil.debug("Scrolled to element with accessibility ID: " + accessibilityId);
            } catch (Exception e) {
                LoggerUtil.error("Failed to scroll to element by accessibility ID: " + accessibilityId, e);
                throw new RuntimeException("Scroll failed: " + e.getMessage(), e);
            }
        }
    }

    public boolean waitForElementByText(String text, int timeoutInSeconds) {
        try {
            LoggerUtil.debug("Waiting for interactable element with text: " + text);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            WebElement element = wait.until(driver -> {
                try {
                    WebElement foundElement = findByText(text);
                    return (foundElement.isDisplayed() && foundElement.isEnabled()) ? foundElement : null;
                } catch (Exception e) {
                    return null;
                }
            });

            boolean isVisible = element != null; // true if element is found and visible
            LoggerUtil.debug(isVisible
                    ? "Element with text is interactable: " + text
                    : "Element with text not found or not interactable: " + text);
            return isVisible;
        } catch (TimeoutException e) {
            LoggerUtil.error("Timeout: Element with text not interactable: " + text, e);
            return false; // return false if timeout occurs
        } catch (Exception e) {
            LoggerUtil.error("Unexpected error waiting for element with text: " + text, e);
            return false;
        }
    }

    public boolean waitForElementByAccessibilityId(String accessibilityId, int timeoutInSeconds) {
        try {
            LoggerUtil.debug("Waiting for interactable element with Accessibility ID: " + accessibilityId);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            WebElement element = wait.until(driver -> {
                try {
                    WebElement foundElement = findByAccessibilityId(accessibilityId);
                    return (foundElement.isDisplayed() && foundElement.isEnabled()) ? foundElement : null;
                } catch (Exception e) {
                    return null;
                }
            });

            boolean isVisible = element != null; // true if element is found and visible
            LoggerUtil.debug(isVisible
                    ? "Element is interactable with Accessibility ID: " + accessibilityId
                    : "Element not found or not interactable with Accessibility ID: " + accessibilityId);
            return isVisible;
        } catch (TimeoutException e) {
            LoggerUtil.error("Timeout: Element not interactable with Accessibility ID: " + accessibilityId, e);
            return false; // return false if timeout occurs
        } catch (Exception e) {
            LoggerUtil.error("Unexpected error waiting for element with Accessibility ID: " + accessibilityId, e);
            return false;
        }
    }

    public boolean waitForElementById(String resourceId, int timeoutInSeconds) {
        try {
            LoggerUtil.debug("Waiting for interactable element with Resource ID: " + resourceId);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

            WebElement element = wait.until(driver -> {
                try {
                    WebElement foundElement = findById(resourceId);
                    return (foundElement.isDisplayed() && foundElement.isEnabled()) ? foundElement : null;
                } catch (Exception e) {
                    return null;
                }
            });

            boolean isVisible = element != null;
            LoggerUtil.debug(isVisible
                    ? "Element is interactable with Resource ID: " + resourceId
                    : "Element not found or not interactable with Resource ID: " + resourceId);
            return isVisible;
        } catch (TimeoutException e) {
            LoggerUtil.error("Timeout: Element not interactable with Resource ID: " + resourceId, e);
            return false;
        } catch (Exception e) {
            LoggerUtil.error("Unexpected error waiting for element with Resource ID: " + resourceId, e);
            return false;
        }
    }

}

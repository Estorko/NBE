package com.nbe.automation.base;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class BasePage {
    private final AndroidDriver driver;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
    }

    public WebElement find(By locator) {
        LoggerUtil.debug("Finding element : " + locator.toString(), this.getClass());
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void set(By locater, String text) {
        LoggerUtil.debug(String.format("Sending keys '%s' to locator %s ", text, locater.toString()), this.getClass());
        WebElement element = find(locater);
        element.clear();
        element.sendKeys(text);
    }

    public void enter() {
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until((Function<WebDriver, Boolean>) d -> {
                    try {
                        return !((AndroidDriver) d).isKeyboardShown();
                    } catch (Exception e) {
                        return true;
                    }
                });
    }

    public boolean isDisplayed(By locator) {
        try {
            LoggerUtil.debug("Checking visibility of element with locator: " + locator, this.getClass());
            return driver.findElement(locator).isDisplayed();

        } catch (Exception e) {
            LoggerUtil.warn("Element not found for locator: " + locator, this.getClass());
            return false;
        }
    }

    public void scrollDown() {
        try {
            LoggerUtil.debug("Starting scroll down", this.getClass());
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
            LoggerUtil.debug("Scroll down completed", this.getClass());

        } catch (Exception e) {
            LoggerUtil.error("Scroll down failed: " + e.getMessage(), this.getClass());
            throw new RuntimeException("Failed to scroll down: " + e.getMessage(), e);
        }
    }

    public void scrollUp() {
        try {
            LoggerUtil.debug("Starting scroll up", this.getClass());
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
            LoggerUtil.debug("Scroll up completed", this.getClass());

        } catch (Exception e) {
            LoggerUtil.error("Scroll up failed: " + e.getMessage(), this.getClass());
            throw new RuntimeException("Failed to scroll up: " + e.getMessage(), e);
        }
    }

    public WebElement findByPartialContentDesc(By locator, WebElement parent) {
        LoggerUtil.debug("Finding element within parent using partial content-desc via locator: " + locator,
                getClass());
        String value = extractLocatorValue(locator.toString()).toLowerCase();
        String xpath = ".//*[@content-desc and contains(translate(@content-desc, " +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + value + "')]";

        return parent.findElement(By.xpath(xpath));
    }

    public void scrollToElement(By locator) {
        LoggerUtil.debug("Attempting to scroll to element: " + locator, this.getClass());
        try {
            // if (isDisplayed(locator)) {
            //     LoggerUtil.debug("Element is already visible, no need to scroll", this.getClass());
            //     return;
            // }
            String uiScrollable = "new UiScrollable(new UiSelector().scrollable(true))"
                    + ".setAsVerticalList()"
                    + ".scrollForward()";
            if (locator instanceof AppiumBy.ByAccessibilityId) {
                String accessibilityId = extractLocatorValue(locator.toString());
                LoggerUtil.info("Scrolling to element with AccessibilityId: " + accessibilityId, getClass());
                uiScrollable += ".scrollIntoView(new UiSelector().description(\"" + accessibilityId + "\"))";
            } else if (locator instanceof AppiumBy.ById) {
                String resourceId = extractLocatorValue(locator.toString());
                LoggerUtil.info("Scrolling to element with ResourceId: " + resourceId, getClass());
                uiScrollable += ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))";
            } else if (locator instanceof AppiumBy.ByAndroidUIAutomator) {
                String text = extractLocatorValue(locator.toString());
                LoggerUtil.info("Scrolling by text: " + text, getClass());
                uiScrollable += ".scrollIntoView(new UiSelector().text(\"" + text + "\"))";
            } else if (locator instanceof By.ByXPath) {
                String xpath = extractLocatorValue(locator.toString());
                LoggerUtil.info("Scrolling by XPath: " + xpath, getClass());
                uiScrollable += ".scrollIntoView(new UiSelector().xpath(\"" + xpath + "\"))";
            } else if (locator instanceof AppiumBy.ByClassName) {
                String className = extractLocatorValue(locator.toString());
                LoggerUtil.info("Scrolling by ClassName: " + className, getClass());
                uiScrollable += ".scrollIntoView(new UiSelector().className(\"" + className + "\"))";
            } else {
                // Fallback scroll using swipe (up to 5 times) if locator is not recognized
                int maxScrolls = 5;
                boolean found = false;

                for (int i = 0; i < maxScrolls; i++) {
                    if (isDisplayed(locator)) {
                        found = true;
                        break;
                    }
                    scrollDown();
                }

                if (!found) {
                    throw new RuntimeException("Element not found after scrolling.");
                }
                return;
            }
            driver.findElement(AppiumBy.androidUIAutomator(uiScrollable));
            LoggerUtil.debug("Successfully scrolled to element: " + locator, this.getClass());

        } catch (Exception e) {
            LoggerUtil.error("Failed to scroll to element: " + locator, e, this.getClass());
            throw new RuntimeException("Scroll failed: " + e.getMessage(), e);
        }
    }

    private String extractLocatorValue(String locatorString) {
        if (locatorString.contains(": ")) {
            return locatorString.substring(locatorString.indexOf(": ") + 2);
        }
        return locatorString;
    }

    public boolean waitForElement(By locator, int timeoutInSeconds) {
        try {
            LoggerUtil.debug("Waiting for interactable element: " + locator, this.getClass());

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            WebElement element = wait.until(driver1 -> {
                try {
                    WebElement foundElement = driver.findElement(locator);
                    return (foundElement.isDisplayed() && foundElement.isEnabled()) ? foundElement : null;
                } catch (Exception e) {
                    return null;
                }
            });

            boolean isVisible = element != null;
            LoggerUtil.debug(isVisible
                    ? "Element is interactable: " + locator
                    : "Element not found or not interactable: " + locator, this.getClass());
            return isVisible;

        } catch (TimeoutException e) {
            LoggerUtil.error("Timeout: Element not interactable: " + locator, e, this.getClass());
            return false;
        } catch (Exception e) {
            LoggerUtil.error("Unexpected error waiting for element: " + locator, e, this.getClass());
            return false;
        }
    }

    // Todo: refactor for locators instead
    public String getTextFromChildIndex(String className, int parentInstance, int childIndex) {
        try {
            LoggerUtil.debug("Getting text from child index " + childIndex + " of element with class: " + className
                    + " and instance: " + parentInstance, this.getClass());
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

    // Todo: refactor for locators instead
    public WebElement findByAccessibilityIdWithinParent(WebElement parent, String accessibilityId) {
        LoggerUtil.debug("Finding element by Accessibility ID within parent: " + accessibilityId, this.getClass());
        return parent
                .findElement(AppiumBy.androidUIAutomator("new UiSelector().description(\"" + accessibilityId + "\")"));
    }

    // Todo: refactor for locators instead
    public WebElement findByClassNameAndInstance(String className, int instance) {
        try {
            LoggerUtil.debug("Finding element by class name: " + className + " and instance: " + instance,
                    this.getClass());
            return driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"" + className + "\").instance(" + instance + ")"));
        } catch (Exception e) {
            LoggerUtil.error("Error finding element by class name and instance: " + e.getMessage(), e);
            throw new RuntimeException("Failed to find element with class: " + className + " and instance: " + instance,
                    e);
        }
    }
}

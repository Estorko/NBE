# NBE Appium Automation (Java)

This project demonstrates how to automate the NBE mobile app using Appium and Java.

## Prerequisites

1. Java 11 or higher installed
2. Maven installed
3. Java Development Kit (JDK) 8 or higher
4. Android Studio and Android SDK
5. Appium Server installed
6. A physical Android device or emulator

## Setup

1. Install Appium Server:
```bash
npm install -g appium
```

2. Install Appium Doctor to verify your setup:
```bash
npm install -g appium-doctor
```

3. Run Appium Doctor to check your setup:
```bash
appium-doctor
```

4. Start the Appium server:
```bash
appium
```

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── nbe/
│               └── automation/
│                   ├── base/
│                   │   └── BaseTest.java
│                   └── utils/
│                       └── AppiumUtils.java
└── test/
    └── java/
        └── com/
            └── nbe/
                └── automation/
                    └── NBETest.java
```

## Configuration

Before running the tests, ensure your Android device/emulator is connected and verify:
1. Device name (run `adb devices`)
2. App package: `com.ofss.obdx.and.nbe.com.eg`
3. App activity: `com.ofss.digx.mobile.android.SplashActivity`

## Running Tests

1. Make sure Appium server is running
2. Run all tests:
```bash
mvn clean test
```

3. Run specific test class:
```bash
mvn test -Dtest=NBETest
```

## Finding Elements

You can find elements using various locators in the `AppiumUtils` class:
```java
// By Accessibility ID
appiumUtils.waitForElement("elementId");

// Click element
appiumUtils.clickElement("elementId");

// Input text
appiumUtils.sendKeys("elementId", "text");

// Get text
String text = appiumUtils.getText("elementId");

// Check visibility
boolean isVisible = appiumUtils.isElementDisplayed("elementId");
```

## Common Commands

- Click: `appiumUtils.clickElement("elementId")`
- Input text: `appiumUtils.sendKeys("elementId", "text")`
- Get text: `appiumUtils.getText("elementId")`
- Wait for element: `appiumUtils.waitForElement("elementId")`
- Check visibility: `appiumUtils.isElementDisplayed("elementId")` 
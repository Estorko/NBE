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
│                   │   └── TestEnvironment.java
│                   └── utils/
│                       ├── AppiumUtils.java
│                       ├── ConfigReader.java
│                       └── LoggerUtil.java
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
3. App activity: `com.ofss.digx.mobile.android.MainActivity`

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

## Test Flow

The test suite includes the following test cases:

1. Login with valid credentials
2. Select account and open Account Overview
3. Get and mask account number and IBAN
4. Verify scroll functionality
5. Logout from the application

## Finding Elements

You can find elements using various locators in the `AppiumUtils` class:

```java
// By Accessibility ID
appiumUtils.findByAccessibilityId("elementId");

// By Text
appiumUtils.findByText("text");

// By ID
appiumUtils.findById("elementId");

// By Class Name and Instance
appiumUtils.findByClassNameAndInstance("className", instance);

// Click element
appiumUtils.clickByText("text");
appiumUtils.clickById("elementId");
appiumUtils.clickByAccessibilityId("elementId");

// Input text
appiumUtils.sendKeysById("elementId", "text");

// Check visibility
boolean isVisible = appiumUtils.isDisplayedByText("text");
```

## Logging

The project uses Log4j2 for logging. Logs are written to both console and file:

- Console output
- File: `logs/NBEAutomation.log`

## Error Handling

All tests include proper error handling:

- Detailed error logging
- Test failures with descriptive messages
- Proper cleanup after each test

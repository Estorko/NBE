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
NBE Appium Automation
---------------------
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── nbe
    │   │           └── automation
    │   │               ├── base
    │   │               │   └── BasePage.java
    │   │               ├── config
    │   │               │   ├── AppiumServerManager.java
    │   │               │   ├── AppProperties.java
    │	│	            │   ├── DriverFactory.java
    │   │               │   ├── EmulatorManager.java
    │   │               │   └── TestLauncher.java
    │   │               ├── pages
    │   │               │   └── youtube
    │   │               │       ├── ChannelPage.java
    │   │               │       ├── HomePage.java
    │   │               │       └── SearchResultsPage.java
    │   │               └── utils
    │   │                   └── LoggerUtil.java
    │   └── resources
    │       ├── application.properties
    │       └── log4j2.xml
    └── test
        ├── java
        │   └── com
        │       └── nbe
        │           └── automation
        │               └── tests
        │                   ├── base
        │                   │    └── BaseTest.java
        │                   ├── YoutubeParallelTest1.java
        │                   ├── YoutubeParallelTest2.java
        │                   ├── YoutubeParallelTest3.java
        │                   ├── YoutubeParallelTest4.java
        │                   ├── YoutubeParallelTest5.java
        │                   └── YoutubeParallelTest6.java
        └── resources
            └── junit-platform.properties          
```
## Running Tests

1. Make sure Appium server is running
2. Run all tests:

```bash
mvn clean test
```

3. Run specific test class:

```bash
mvn test -Dtest=YoutubeParallelTest1
```
## Logging

The project uses Log4j2 for logging. Logs are written to both console and file:

- Console output
- File: `logs/NBEAutomation.log`


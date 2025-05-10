package com.nbe.automation.config;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;

public class DriverFactory {

    private static final Map<String, AndroidDriver> drivers = new ConcurrentHashMap<>();
    private final AppProperties appProperties;

    public DriverFactory(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DriverFactory::quitAll));
    }

    public AndroidDriver createDriver(String deviceName, String udid, String serverUrl,
            int systemPort, int chromePort) {
        AndroidDriver driver = drivers.get(udid);
        if (driver != null) {
            LoggerUtil.info("Driver already exists for UDID: " + udid, this.getClass());
            return driver;
        }
        try {
            LoggerUtil.info(String.format("Creating driver for emulator: [%s].", udid), this.getClass());
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", appProperties.getPlatformName());
            caps.setCapability("automationName", appProperties.getAutomationName());
            caps.setCapability("deviceName", deviceName);
            caps.setCapability("udid", udid);
            caps.setCapability("appPackage", appProperties.getYoutubeAppPackage());
            caps.setCapability("appActivity", appProperties.getYoutubeAppActivity());
            caps.setCapability("noReset", appProperties.getNoReset());
            caps.setCapability("newCommandTimeout", appProperties.getNewCommandTimeout());
            caps.setCapability("autoGrantPermissions", appProperties.getAutoGrantPermissions());
            caps.setCapability("ignoreUnimportantViews", true);
            caps.setCapability("appWaitActivity", String.format("%s.*", appProperties.getYoutubeAppPackage()));
            caps.setCapability("appium:systemPort", systemPort);
            caps.setCapability("appium:chromeDriverPort", chromePort);

            driver = new AndroidDriver(new URI(serverUrl).toURL(), caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            waitUntilDriverIsReady(driver, udid);
            LoggerUtil.info(String.format("Driver created successfully for [%s]", udid), this.getClass());
            drivers.put(udid, driver);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to initialize driver for emulator: [%s].", udid), e);
        }
        return driver;
    }

    public AndroidDriver getDriver(String udid) {
        AndroidDriver driver = drivers.get(udid);
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized for UDID: " + udid);
        }
        return driver;
    }

    private void waitUntilDriverIsReady(AndroidDriver driver, String udid) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        String expectedActivity = appProperties.getYoutubeAppActivity();
        String expectedActivitySimple = expectedActivity.startsWith(appProperties.getYoutubeAppPackage())
                ? expectedActivity.replaceFirst(appProperties.getYoutubeAppPackage(), "")
                : expectedActivity;

        try {
            wait.until(d -> {
                try {
                    String currentActivity = driver.currentActivity();
                    return currentActivity != null && !currentActivity.isEmpty() &&
                            (currentActivity.equals(expectedActivitySimple) ||
                                    currentActivity.equals(expectedActivity) ||
                                    currentActivity.endsWith(expectedActivitySimple.replace(".", "")));
                } catch (Exception e) {
                    return false;
                }
            });
            LoggerUtil.info(String.format("Driver is fully ready and active for emulator: [%s].", udid),
                    DriverFactory.class);
        } catch (Exception e) {
            LoggerUtil.error(String.format("Driver failed to be ready within timeout for emulator: [%s].", udid), e,
                    DriverFactory.class);
        }
    }

    public static void quitAll() {
        drivers.values().forEach(driver -> {
            try {
                driver.quit();
            } catch (Exception e) {
                LoggerUtil.warn("Error quitting driver: " + e.getMessage(), DriverFactory.class);
            }
        });
    }
}

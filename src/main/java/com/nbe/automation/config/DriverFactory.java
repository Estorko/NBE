package com.nbe.automation.config;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.nbe.automation.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;

@Getter
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
            LoggerUtil.info("Creating driver for device: " + deviceName, this.getClass());
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
            LoggerUtil.info(String.format("Driver created successfully for [%s]", udid), this.getClass());
            drivers.put(udid, driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver", e);
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

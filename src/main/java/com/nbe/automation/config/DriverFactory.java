package com.nbe.automation.config;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import com.nbe.automation.core.utils.LoggerUtil;
import io.appium.java_client.android.AndroidDriver;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

@Component
@Getter
public class DriverFactory {

    private final Map<String, AndroidDriver> drivers = new ConcurrentHashMap<>();
    private final AppProperties appProperties;

    public DriverFactory(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public AndroidDriver createDriver(String deviceName, String udid, String serverUrl,
            int systemPort, int chromePort) {
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
            caps.setCapability("enforceXPath1", true);
            caps.setCapability("ignoreHiddenApiPolicyError", true);

            caps.setCapability("appWaitActivity", String.format("%s.*", appProperties.getYoutubeAppPackage()));
            caps.setCapability("appium:systemPort", systemPort);
            caps.setCapability("appium:chromeDriverPort", chromePort);

            AndroidDriver driver = new AndroidDriver(new URI(serverUrl).toURL(), caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            drivers.put(udid, driver);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }

    public AndroidDriver getDriver(String udid) {
        AndroidDriver driver = drivers.get(udid);
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized for UDID: " + udid);
        }
        return driver;
    }

    public boolean isDriverAvailable(String udid) {
        return drivers.containsKey(udid);
    }

    @PreDestroy
    public void quitAll() {
        drivers.values().forEach(driver -> {
            try {
                driver.quit();
            } catch (Exception e) {
                LoggerUtil.warn("Error quitting driver: " + e.getMessage(), this.getClass());
            }
        });
    }
}

package com.nbe.automation.base;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.nbe.automation.core.utils.LoggerUtil;
import io.appium.java_client.android.AndroidDriver;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

@Configuration
@Getter
public class AppiumConfig {

    private AndroidDriver driver;
    private static final Object lock = new Object();

    @Value("${appium.automationName}")
    private String automationName;

    @Value("${appium.autoGrantPermissions}")
    private String autoGrantPermissions;

    @Value("${appium.deviceName}")
    private String deviceName;

    @Value("${appium.newCommandTimeout}")
    private String newCommandTimeout;

    @Value("${appium.noReset}")
    private String noReset;

    @Value("${appium.platformName}")
    private String platformName;

    @Value("${appium.serverURL}")
    private String serverURL;

    @Value("${appium.udid}")
    private String udid;

    @Value("${nbe.appium.appActivity}")
    private String nbeAppActivity;

    @Value("${nbe.appium.appPackage}")
    private String nbeAppPackage;

    @Value("${nbe.user.id}")
    private String nbeUserId;

    @Value("${nbe.user.password}")
    private String nbeUserPassword;

    @Value("${youtube.appium.appActivity}")
    private String youtubeAppActivity;

    @Value("${youtube.appium.appPackage}")
    private String youtubeAppPackage;

    @Bean(destroyMethod = "")
    // @Scope("singleton")
    @Scope("prototype")
    public AndroidDriver getDriver() {
        synchronized (lock) {
            if (driver == null) {
                try {
                    LoggerUtil.info("Initializing test environment");
                    // System.setProperty("webdriver.http.factory", "apache");
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.setCapability("platformName", platformName);
                    capabilities.setCapability("automationName", automationName);
                    capabilities.setCapability("deviceName", deviceName);
                    capabilities.setCapability("udid", udid);
                    capabilities.setCapability("appPackage", youtubeAppPackage);
                    capabilities.setCapability("appActivity", youtubeAppActivity);
                    capabilities.setCapability("noReset", noReset);
                    capabilities.setCapability("newCommandTimeout", newCommandTimeout);
                    capabilities.setCapability("autoGrantPermissions", autoGrantPermissions);
                    capabilities.setCapability("ignoreUnimportantViews", true);
                    capabilities.setCapability("enforceXPath1", true);

                    LoggerUtil.info("Connecting to Appium server at: " + serverURL);
                    this.driver = new AndroidDriver(new URI(serverURL).toURL(), capabilities);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
                    LoggerUtil.info("Current session: " + driver.getSessionId());
                    LoggerUtil.info("Test environment initialized");
                    return driver;
                } catch (Exception e) {
                    throw new RuntimeException("Failed to initialize AndroidDriver", e);
                }
            } else {
                LoggerUtil.info("Reusing existing AndroidDriver session");
            }
            return driver;
        }
    }

    private void startEmulator() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("emulator", "-avd", "YourEmulatorName"); // Replace with your emulator name
            processBuilder.start();
            Thread.sleep(30000); // Wait for the emulator to boot up
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //not tested
    public void updateAppPackageAndActivity(String appPackage, String appActivity) {
        this.nbeAppPackage = appPackage;
        this.nbeAppActivity = appActivity;
        reinitializeDriver();
    }

    //not tested
    private void reinitializeDriver() {
        synchronized (lock) {
            try {
                if (driver != null) {
                    driver.quit();
                    driver = null;
                }
                LoggerUtil.info("Reinitializing driver with updated app package and activity");
                getDriver();
            } catch (Exception e) {
                LoggerUtil.error("Error while reinitializing driver: " + e.getMessage(), e);
            }
        }
    }

    public String getNbeUserId() {
        return this.nbeUserId;
    }

    public String getNbeUserPassword() {
        return this.nbeUserPassword;
    }

    @PreDestroy
    public void quitDriver() {
        synchronized (lock) {
            if (driver != null) {
                try {
                    // Check if the session is still valid before attempting to quit
                    if (driver.getSessionId() != null && driver.getSessionId().toString().length() > 0) {
                        driver.quit(); // Only quit if the session is still valid
                        LoggerUtil.info("Driver session terminated successfully");
                    } else {
                        LoggerUtil.info("Session is already terminated or invalid, no need to quit");
                    }
                } catch (UnsupportedCommandException e) {
                    // Handle situation when session is already terminated or unreachable
                    LoggerUtil.warn("Attempted to quit driver, but session was already terminated: " + e.getMessage());
                } catch (Exception e) {
                    LoggerUtil.error("Error during driver cleanup: " + e.getMessage(), e);
                } finally {
                    driver = null;
                }
            } else {
                LoggerUtil.info("No active driver session to terminate");
            }
        }
    }
}

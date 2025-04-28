package com.nbe.automation.base;

import com.nbe.automation.utils.LoggerUtil;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class TestEnvironment {
    protected AndroidDriver<MobileElement> driver;

    @BeforeMethod
    public void setUp() throws Exception {
        LoggerUtil.info("Initializing test environment");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "R5CT10LMD3L");
        capabilities.setCapability("udid", "R5CT10LMD3L");
        capabilities.setCapability("appPackage", "com.ofss.obdx.and.nbe.com.eg");
        capabilities.setCapability("appActivity", "com.ofss.digx.mobile.android.MainActivity");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("newCommandTimeout", 60);
        capabilities.setCapability("autoGrantPermissions", true);

        driver = new AndroidDriver<>(URI.create("http://localhost:4723").toURL(), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        LoggerUtil.info("Test environment initialized");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            LoggerUtil.info("Test environment cleaned up");
        }
    }
} 
package com.nbe.automation.base;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AndroidDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        // Set desired capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("appPackage", "com.ofss.obdx.and.nbe.com.eg");
        capabilities.setCapability("appActivity", "com.ofss.digx.mobile.android.SplashActivity");
        capabilities.setCapability("noReset", true);

        // Initialize the driver
        driver = new AndroidDriver(new URL("http://localhost:4723"), capabilities);
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        // Quit the driver after each test
        if (driver != null) {
            driver.quit();
        }
    }
} 
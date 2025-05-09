package com.nbe.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private final Properties properties;

    public AppProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties", e);
        }
    }

    public String getAndroidHomeEnv() {
        return properties.getProperty("android.home.env");
    }

    public String getAutomationName() {
        return properties.getProperty("appium.automationName");
    }

    public String getAutoGrantPermissions() {
        return properties.getProperty("appium.autoGrantPermissions");
    }

    public String getNewCommandTimeout() {
        return properties.getProperty("appium.newCommandTimeout");
    }

    public String getNoReset() {
        return properties.getProperty("appium.noReset");
    }

    public String getPlatformName() {
        return properties.getProperty("appium.platformName");
    }

    public String getServerURL() {
        return properties.getProperty("appium.serverURL");
    }

    public String getEmulatorNames() {
        return properties.getProperty("emulator.names");
    }

    public String getYoutubeAppActivity() {
        return properties.getProperty("youtube.appium.appActivity");
    }

    public String getYoutubeAppPackage() {
        return properties.getProperty("youtube.appium.appPackage");
    }
}

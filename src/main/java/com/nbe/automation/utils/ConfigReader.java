package com.nbe.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "testdata.properties";

    public static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                LoggerUtil.error("Unable to find " + CONFIG_FILE);
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
            properties.load(input);
            LoggerUtil.info("Configuration file loaded successfully");
        } catch (IOException e) {
            LoggerUtil.error("Error loading configuration file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            LoggerUtil.error("Property not found: " + key);
            throw new RuntimeException("Property not found: " + key);
        }
        LoggerUtil.debug("Retrieved property: " + key + " = " + value);
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        LoggerUtil.debug("Retrieved property: " + key + " = " + value);
        return value;
    }
} 
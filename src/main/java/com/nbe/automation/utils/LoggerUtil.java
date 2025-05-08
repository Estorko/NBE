package com.nbe.automation.utils;

import org.apache.logging.log4j.LogManager;

public class LoggerUtil {

    public static void info(String message, Class<?> clazz) {
        LogManager.getLogger(clazz).info(message);
    }

    public static void debug(String message, Class<?> clazz) {
        LogManager.getLogger(clazz).debug(message);
    }

    public static void warn(String message, Class<?> clazz) {
        LogManager.getLogger(clazz).warn(message);
    }

    public static void error(String message, Exception e) {
        LogManager.getLogger(e).error(message);
    }

    public static void error(String message, Throwable t, Class<?> clazz) {
        LogManager.getLogger(clazz).error(message, t);
    }
    
    public static void error(String message, Class<?> clazz) {
        LogManager.getLogger(clazz).error(message);
    }
}

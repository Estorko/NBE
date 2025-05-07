package com.nbe.automation.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    // // Optional: keep default methods using static logger if needed
    // private static final Logger defaultLogger = LogManager.getLogger("NBEAutomation");

    // public static Logger getLogger() {
    //     return defaultLogger;
    // }

    // public static void info(String message) {
    //     defaultLogger.info(message);
    // }

    // public static void debug(String message) {
    //     defaultLogger.debug(message);
    // }

    // public static void warn(String message) {
    //     defaultLogger.warn(message);
    // }

    // public static void error(String message) {
    //     defaultLogger.error(message);
    // }

    // public static void error(String message, Throwable t) {
    //     defaultLogger.error(message, t);
    // }
}

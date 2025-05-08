package com.nbe.automation.tests.base;

import java.util.concurrent.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.nbe.automation.config.AppProperties;
import com.nbe.automation.config.AppiumServerManager;
import com.nbe.automation.config.DriverFactory;
import com.nbe.automation.config.EmulatorManager;
import com.nbe.automation.config.TestLauncher;
import com.nbe.automation.utils.LoggerUtil;

@Execution(ExecutionMode.SAME_THREAD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private static boolean cleanupDone = false;
    protected static TestLauncher testLauncher;
    protected static DriverFactory driverFactory;
    protected static AppiumServerManager appiumServerManager;
    protected static EmulatorManager emulatorManager;
    protected static CountDownLatch latch;

    static Stream<String> udidsProvider() {
        return TestLauncher.getAssignedUdids().stream();
    }

    @BeforeAll
    static void globalSetup() {
        AppProperties appProperties = new AppProperties();
        driverFactory = new DriverFactory(appProperties);
        emulatorManager = new EmulatorManager(appProperties);
        appiumServerManager = new AppiumServerManager();
        testLauncher = new TestLauncher(emulatorManager, appiumServerManager, driverFactory, appProperties);

        LoggerUtil.info("GLOBAL SETUP", BaseTest.class);
        testLauncher.startAll();
        testLauncher.waitForDrivers(60000);
        latch = new CountDownLatch(1);
    }

    // @AfterAll
    // static void globalTeardown() {
    //     if (!cleanupDone) {
    //         LoggerUtil.info("Shutting down all resources", BaseTest.class);
    //         appiumServerManager.killAllAppiumServers();
    //         emulatorManager.killAllEmulators();
    //         cleanupDone = true;
    //     }
    // }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtil.info("JVM shutdown triggered. Cleaning up resources...", BaseTest.class);
            try {
                if (appiumServerManager != null) {
                    appiumServerManager.killAllAppiumServers();
                }
                if (emulatorManager != null) {
                    emulatorManager.killAllEmulators();
                }
            } catch (Exception e) {
                LoggerUtil.error("Error during shutdown cleanup", e, BaseTest.class);
            }
        }));
    }
}

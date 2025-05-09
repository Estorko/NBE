package com.nbe.automation.tests.base;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
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

    protected static TestLauncher testLauncher;
    protected static DriverFactory driverFactory;
    protected static AppiumServerManager appiumServerManager;
    protected static EmulatorManager emulatorManager;
    protected static CountDownLatch latch;

    static Stream<String> udidsProvider() {
        List<String> udids = TestLauncher.getAssignedUdids();
        LoggerUtil.info("UDIDs provided to test: " + udids, BaseTest.class);
        return udids.stream();
    }

    @BeforeAll
    static void globalSetup() {
        AppProperties appProperties = new AppProperties();
        driverFactory = new DriverFactory(appProperties);
        emulatorManager = new EmulatorManager(appProperties);
        appiumServerManager = new AppiumServerManager();
        testLauncher = new TestLauncher(emulatorManager, appiumServerManager, driverFactory, appProperties);

        LoggerUtil.info("-> STARTING GLOBAL SETUP <-", BaseTest.class);
        testLauncher.startAll();
        testLauncher.waitForDrivers(60000);
        latch = new CountDownLatch(1);
    }

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

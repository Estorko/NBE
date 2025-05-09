package com.nbe.automation.tests.base;

import java.util.concurrent.CountDownLatch;

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
    private static volatile boolean setupComplete = false;

    protected static String acquireUdid() {
        try {
            return TestLauncher.acquireUdid();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not acquire Udid from Testlauncher");
        }
    }

    protected static void releaseUdid(String udid) {
        TestLauncher.registerAndReleaseUdid(udid);
    }

    protected boolean isWaitingForUdid() {
        return TestLauncher.isWaitingForUdid();

    }

    @BeforeAll
    static synchronized void globalSetup() {
        if (!setupComplete) {
            AppProperties appProperties = new AppProperties();
            driverFactory = new DriverFactory(appProperties);
            emulatorManager = new EmulatorManager(appProperties);
            appiumServerManager = new AppiumServerManager();
            testLauncher = new TestLauncher(emulatorManager, appiumServerManager, driverFactory, appProperties);

            LoggerUtil.info("-> STARTING GLOBAL SETUP <-", BaseTest.class);
            testLauncher.startAll();
            latch = new CountDownLatch(1);
            setupComplete = true;
        }
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

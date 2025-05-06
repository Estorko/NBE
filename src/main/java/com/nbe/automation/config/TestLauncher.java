package com.nbe.automation.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.nbe.automation.core.utils.LoggerUtil;

import jakarta.annotation.PostConstruct;

@Component
public class TestLauncher {

    private final EmulatorManager emulatorManager;
    private final AppiumServerManager appiumServerManager;
    private final DriverFactory driverFactory;
    private final List<String> emulatorNames;
    private static final int BASEPORT = 4723;
    private final CountDownLatch driverReadyLatch;
    private final List<String> assignedUdids = Collections.synchronizedList(new ArrayList<>());
    private final AppProperties appProperties;

    public TestLauncher(EmulatorManager emulatorManager, AppiumServerManager appiumServerManager,
            DriverFactory driverFactory, AppProperties appProperties) {
        this.emulatorManager = emulatorManager;
        this.appiumServerManager = appiumServerManager;
        this.driverFactory = driverFactory;
        this.appProperties = appProperties;
        this.emulatorNames = List.of(appProperties.getEmulatorNames().split(","));
        this.driverReadyLatch = new CountDownLatch(emulatorNames.size());
    }

    public void waitForDrivers(long timeoutMillis) {
        try {
            LoggerUtil.info("Waiting for all drivers to be initialized...");
            boolean allDriversReady = driverReadyLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);

            if (!allDriversReady) {
                LoggerUtil.error("Timeout reached while waiting for all drivers to be initialized.");
                appiumServerManager.killAllAppiumServers();
                emulatorManager.killAllEmulators();
                System.exit(1);
            } else {
                LoggerUtil.info("All drivers are ready.");
            }
        } catch (InterruptedException e) {
            LoggerUtil.error("Waiting for drivers was interrupted.");
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    @PostConstruct
    public void startAll() {
        ExecutorService executor = Executors.newFixedThreadPool(emulatorNames.size());

        for (int i = 0; i < emulatorNames.size(); i++) {
            final int index = i;
            executor.submit(() -> {
                String emulatorName = emulatorNames.get(index);
                int emulatorPort = 5554 + (index * 2);
                int appiumPort = BASEPORT + index;
                int bootstrapPort = 8200 + index;
                int chromePort = 9515 + index;

                try {
                    String udid = String.format("emulator-%d", emulatorPort);
                    emulatorManager.startEmulator(emulatorName, emulatorPort);

                    boolean online = emulatorManager.waitForDeviceOnline(udid, 60000);
                    if (!online) {
                        throw new RuntimeException("Device " + udid + " did not appear in adb.");
                    }

                    boolean booted = emulatorManager.waitForBootCompletion(udid, 60000);
                    if (!booted) {
                        throw new RuntimeException("Device " + udid + " did not complete boot.");
                    }
                    // Thread.sleep(30000);
                    appiumServerManager.startAppiumServer(appiumPort, bootstrapPort, chromePort);
                    String serverUrl = String.format("http://127.0.0.1:%d/wd/hub", appiumPort);
                    driverFactory.createDriver(emulatorName, udid, serverUrl, bootstrapPort, chromePort);

                    assignedUdids.add(udid);
                    LoggerUtil.info("Driver created successfully for " + udid);
                    driverReadyLatch.countDown();

                } catch (Exception e) {
                    LoggerUtil.error("Error during setup for " + emulatorName + ": " + e.getMessage(), e);
                }
            });
        }

        shutdownExecutorService(executor);
    }

    private void shutdownExecutorService(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public List<String> getAssignedUdids() {
        return assignedUdids;
    }
}

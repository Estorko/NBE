package com.nbe.automation.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nbe.automation.core.utils.LoggerUtil;

import io.appium.java_client.android.AndroidDriver;
import jakarta.annotation.PostConstruct;

@Component
public class TestLauncher {

    private final EmulatorManager emulatorManager;
    private final AppiumServerManager appiumServerManager;
    private final DriverFactory driverFactory;

    private final List<String> emulatorNames = List.of("nexus_5_api_29_yt_5", "nexus_5_api_29_yt_4");
    private final int basePort = 4723;

    private final CountDownLatch driverReadyLatch = new CountDownLatch(emulatorNames.size());

    public TestLauncher(EmulatorManager emulatorManager, AppiumServerManager appiumServerManager,
            DriverFactory driverFactory) {
        this.emulatorManager = emulatorManager;
        this.appiumServerManager = appiumServerManager;
        this.driverFactory = driverFactory;
    }

    public void waitForDrivers() {
        try {
            LoggerUtil.info("Waiting for all drivers to be initialized...");
            driverReadyLatch.await();
            LoggerUtil.info("All drivers are ready.");
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for drivers to be ready", e);
        }
    }

    @PostConstruct
    public void startAll() {
        ExecutorService executor = Executors.newFixedThreadPool(emulatorNames.size());

        for (int i = 0; i < emulatorNames.size(); i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    String emulatorName = emulatorNames.get(index);
                    int emulatorPort = 5554 + (index * 2);
                    int appiumPort = basePort + index;
                    int bootstrapPort = 8200 + index;
                    int chromePort = 9515 + index;

                    emulatorManager.startEmulator(emulatorName, emulatorPort);
                    Thread.sleep(30000); // Give emulator time to boot

                    // Wait for Appium server to be ready
                    appiumServerManager.startAppiumServer(appiumPort, bootstrapPort, chromePort);
                    waitForAppiumServer(appiumPort);

                    String udid = String.format("emulator-%d", emulatorPort);
                    String serverUrl = String.format("http://127.0.0.1:%d/wd/hub", appiumPort);

                    driverFactory.createDriver(emulatorName, udid, serverUrl, bootstrapPort, chromePort);
                    LoggerUtil.info("Driver created successfully for " + udid);
                    driverReadyLatch.countDown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void waitForAppiumServer(int port) throws InterruptedException {
        boolean appiumReady = false;
        int retries = 10;
        while (retries-- > 0) {
            if (appiumServerManager.isAppiumServerRunning(port)) {
                appiumReady = true;
                break;
            }
            Thread.sleep(3000);
        }
        if (!appiumReady) {
            throw new RuntimeException("Appium server not responding on port " + port);
        }
    }
}

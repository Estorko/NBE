package com.nbe.automation.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.nbe.automation.utils.LoggerUtil;

public class TestLauncher {

    private static final Object LOCK = new Object();
    private static boolean initialized = false;
    private final EmulatorManager emulatorManager;
    private final AppiumServerManager appiumServerManager;
    private final DriverFactory driverFactory;
    private final List<String> emulatorNames;
    private final AppProperties appProperties;
    private static final BlockingQueue<String> udidPool = new LinkedBlockingQueue<>();
    private static final int BASEPORT = 4723;

    public TestLauncher(EmulatorManager emulatorManager, AppiumServerManager appiumServerManager,
            DriverFactory driverFactory, AppProperties appProperties) {
        this.emulatorManager = emulatorManager;
        this.appiumServerManager = appiumServerManager;
        this.appProperties = appProperties;
        this.driverFactory = driverFactory;
        this.emulatorNames = List.of(appProperties.getEmulatorNames().split(","));
    }

    public void startAll() {
        synchronized (LOCK) {
            if (initialized)
                return;

            udidPool.clear();
            ExecutorService executor = Executors.newFixedThreadPool(emulatorNames.size());
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < emulatorNames.size(); i++) {
                final int index = i;
                futures.add(executor.submit(() -> {
                    String emulatorName = emulatorNames.get(index);
                    int emulatorPort = 5554 + (index * 2);
                    int appiumPort = BASEPORT + index;
                    int bootstrapPort = 8200 + index;
                    int chromePort = 9515 + index;

                    try {
                        String udid = String.format("emulator-%d", emulatorPort);
                        emulatorManager.startEmulator(emulatorName, emulatorPort);
                        appiumServerManager.startAppiumServer(appiumPort, bootstrapPort, chromePort);
                        driverFactory.createDriver(emulatorName, udid,
                                String.format(appProperties.getServerURL(), appiumPort), bootstrapPort, chromePort);

                        registerAndReleaseUdid(udid);

                    } catch (Exception e) {
                        LoggerUtil.error(String.format("Error during setup for [%s]: %s", emulatorName, e.getMessage()),
                                e, this.getClass());
                    }
                }));
            }

            initialized = true;
            shutdownExecutorService(executor);
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    LoggerUtil.error("Error in task: " + e.getMessage(), e, TestLauncher.class);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
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

    public static void registerAndReleaseUdid(String udid) {
        boolean added = udidPool.offer(udid);
        if (!added) {
            LoggerUtil.error(String.format("Failed to add UDID: [%s] to the pool.", udid), TestLauncher.class);
        }
    }

    public static String acquireUdid() throws InterruptedException {
        return udidPool.take();
    }

    public static boolean isWaitingForUdid() {
        return udidPool.isEmpty();
    }
}

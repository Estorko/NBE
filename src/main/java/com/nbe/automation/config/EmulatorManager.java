package com.nbe.automation.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nbe.automation.utils.LoggerUtil;

public class EmulatorManager {

    private final AppProperties appProperties;

    public EmulatorManager(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public void startEmulator(String avdName, int port) throws IOException {
        String udid = String.format("emulator-%d", port);
        if (isEmulatorVisible(udid)) {
            LoggerUtil.info(String.format("Emulator [%s] is already online and visible. Skipping start.", avdName),
                    this.getClass());
            return;
        }

        String androidHome = System.getenv(appProperties.getAndroidHomeEnv());
        File emulatorDir = new File(androidHome, "emulator");
        // Use full path to emulator.exe
        File emulatorExecutable = new File(emulatorDir, "emulator.exe");

        if (androidHome == null || androidHome.isBlank()) {
            throw new IllegalStateException("ANDROID_HOME environment variable is not set.");
        }

        if (!emulatorDir.exists()) {
            throw new IllegalStateException(
                    "Emulator directory not found in ANDROID_HOME: " + emulatorDir.getAbsolutePath());
        }

        if (!emulatorExecutable.exists()) {
            throw new IllegalStateException("Emulator executable not found: " + emulatorExecutable.getAbsolutePath());
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    emulatorExecutable.getAbsolutePath(),
                    "-avd", avdName,
                    "-port", String.valueOf(port),
                    "-no-snapshot-save",
                    "-no-audio",
                    "-no-boot-anim",
                    "-no-snapshot-load"
            // "-no-window",
            );

            pb.directory(emulatorDir);
            File outputFile = new File("logs/emulatorsLog.log");
            pb.redirectOutput(outputFile);
            pb.redirectError(outputFile);
            LoggerUtil.info(String.format("Starting emulator [%s] on port [%s] from '%s'", avdName, port,
                    emulatorExecutable.getAbsolutePath()), this.getClass());
            pb.start();

            boolean online = waitForDeviceOnline(udid, 60000);
            if (!online) {
                throw new RuntimeException("Device " + udid + " did not appear in adb.");
            }
            boolean booted = waitForBootCompletion(udid, 60000);
            if (!booted) {
                throw new RuntimeException("Device " + udid + " did not complete boot.");
            }
        } catch (Exception e) {
            LoggerUtil.error(String.format("Failed to start Emulator [%s] ", udid), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    public boolean waitForBootCompletion(String emulatorId, long timeoutMillis) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            try {
                Process process = new ProcessBuilder("adb", "-s", emulatorId, "shell", "getprop", "sys.boot_completed")
                        .start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = reader.readLine();
                    if (line != null && line.trim().equals("1")) {
                        LoggerUtil.info(String.format("Emulator [%s] is fully booted.", emulatorId), this.getClass());
                        return true;
                    }
                }
            } catch (IOException e) {
                LoggerUtil.warn("Error checking sys.boot_completed: " + e.getMessage(), this.getClass());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for emulator boot.", this.getClass());
                Thread.currentThread().interrupt();
                return false;
            }
        }

        LoggerUtil.error("Timeout: Emulator " + emulatorId + " did not finish booting.", this.getClass());
        return false;
    }

    public boolean waitForDeviceOnline(String emulatorId, long timeoutMillis) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            if (isEmulatorVisible(emulatorId)) {
                LoggerUtil.info(String.format("Emulator [%s] is now online.", emulatorId), this.getClass());
                return true;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for device online.", this.getClass());
                Thread.currentThread().interrupt();
                return false;
            }
        }
        LoggerUtil.error(String.format("Timeout: Emulator [%s] did not appear online within the timeout.", emulatorId),
                this.getClass());
        return false;
    }

    public void killAllEmulators() {
        try {
            Process listDevices = new ProcessBuilder("cmd.exe", "/c", "adb devices")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(listDevices.getInputStream()))) {
                var lines = reader.lines().toList();

                for (String line : lines) {
                    if (line.startsWith("emulator-")) {
                        String emulatorId = line.split("\\s+")[0];
                        new ProcessBuilder("cmd.exe", "/c", "adb -s " + emulatorId + " emu kill")
                                .start();
                        LoggerUtil.info(String.format("Killed emulator: [%s]", emulatorId), this.getClass());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtil.error("Failed to kill emulators.", this.getClass());
        }
    }

    private boolean isEmulatorVisible(String emulatorId) {
        Process process = null;
        try {
            process = new ProcessBuilder("adb", "devices").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith(emulatorId) && line.contains("device")) {
                        LoggerUtil.info(String.format("Emulator [%s] active in adb devices.", emulatorId),
                                this.getClass());
                        return true;
                    }
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LoggerUtil.warn("adb command failed with exit code: " + exitCode, this.getClass());
            }
        } catch (IOException e) {
            LoggerUtil.warn("Failed to check adb devices: " + e.getMessage(), this.getClass());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerUtil.warn("adb process was interrupted: " + e.getMessage(), this.getClass());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    // not currently used - enhance
    public static void killEmulatorByName(String emulatorId) {
        try {
            Process listDevices = new ProcessBuilder("cmd.exe", "/c", "adb devices")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(listDevices.getInputStream()))) {
                var lines = reader.lines().toList();

                boolean emulatorFound = false;

                for (String line : lines) {
                    if (line.contains(emulatorId)) {
                        new ProcessBuilder("cmd.exe", "/c", "adb -s " + emulatorId + " emu kill")
                                .start();
                        LoggerUtil.info("Killed emulator: " + emulatorId, EmulatorManager.class);
                        emulatorFound = true;
                        break;
                    }
                }

                if (!emulatorFound) {
                    LoggerUtil.warn("Emulator with ID " + emulatorId + " not found.", EmulatorManager.class);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtil.error("Failed to kill emulator: " + emulatorId, EmulatorManager.class);
        }
    }
}

package com.nbe.automation.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.nbe.automation.core.utils.LoggerUtil;

@Component
public class EmulatorManager {

    private final AppProperties appProperties;

    public EmulatorManager(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public void startEmulator(String avdName, int port) throws IOException {
        String androidHome = System.getenv(appProperties.getAndroidHomeEnv());
        if (androidHome == null || androidHome.isBlank()) {
            throw new IllegalStateException("ANDROID_HOME environment variable is not set.");
        }

        File emulatorDir = new File(androidHome, "emulator");
        if (!emulatorDir.exists()) {
            throw new IllegalStateException(
                    "Emulator directory not found in ANDROID_HOME: " + emulatorDir.getAbsolutePath());
        }

        // Use full path to emulator.exe
        File emulatorExecutable = new File(emulatorDir, "emulator.exe"); // Use just "emulator" on Linux/macOS
        if (!emulatorExecutable.exists()) {
            throw new IllegalStateException("Emulator executable not found: " + emulatorExecutable.getAbsolutePath());
        }

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
        LoggerUtil.info(
                "Starting emulator " + avdName + " on port " + port + " from " + emulatorExecutable.getAbsolutePath(), this.getClass());
        pb.start();
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
                        LoggerUtil.info("Emulator " + emulatorId + " is fully booted.", this.getClass());
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
            try {
                Process process = new ProcessBuilder("adb", "devices").start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().startsWith(emulatorId) && line.contains("device")) {
                            LoggerUtil.info("Emulator " + emulatorId + " active in adb devices.", this.getClass());
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                LoggerUtil.warn("Failed to check adb devices: " + e.getMessage(), this.getClass());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for device online.", this.getClass());
                Thread.currentThread().interrupt();
                return false;
            }
        }

        LoggerUtil.error("Timeout: Emulator " + emulatorId + " did not appear in adb devices.", this.getClass());
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
                        LoggerUtil.info("Killed emulator: " + emulatorId, this.getClass());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtil.error("Failed to kill emulators.", this.getClass());
        }
    }

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

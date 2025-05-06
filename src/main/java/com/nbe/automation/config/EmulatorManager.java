package com.nbe.automation.config;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.stream.Collectors;

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
                emulatorExecutable.getAbsolutePath(), "-avd", avdName,
                "-port", String.valueOf(port),
                "-no-snapshot-save", "-no-audio", "-no-boot-anim");

        pb.directory(emulatorDir);
        pb.redirectOutput(Redirect.DISCARD).redirectError(Redirect.DISCARD);
        pb.inheritIO();

        LoggerUtil.info(
                "Starting emulator " + avdName + " on port " + port + " from " + emulatorExecutable.getAbsolutePath());
        pb.start();
    }

    public boolean waitForBootCompletion(String emulatorId, long timeoutMillis) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            try {
                Process process = new ProcessBuilder("adb", "-s", emulatorId, "shell", "getprop", "sys.boot_completed").start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = reader.readLine();
                    if (line != null && line.trim().equals("1")) {
                        LoggerUtil.info("Emulator " + emulatorId + " is fully booted.");
                        return true;
                    }
                }
            } catch (IOException e) {
                LoggerUtil.warn("Error checking sys.boot_completed: " + e.getMessage());
            }
    
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for emulator boot.");
                Thread.currentThread().interrupt();
                return false;
            }
        }
    
        LoggerUtil.error("Timeout: Emulator " + emulatorId + " did not finish booting.");
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
                            LoggerUtil.info("Emulator " + emulatorId + " appeared in adb devices.");
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                LoggerUtil.warn("Failed to check adb devices: " + e.getMessage());
            }
    
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for device online.");
                Thread.currentThread().interrupt();
                return false;
            }
        }
    
        LoggerUtil.error("Timeout: Emulator " + emulatorId + " did not appear in adb devices.");
        return false;
    }
    

    public void killAllEmulators() {
        try {
            Process listDevices = new ProcessBuilder("cmd.exe", "/c", "adb devices")
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(listDevices.getInputStream()))) {
                var lines = reader.lines().collect(Collectors.toList());

                for (String line : lines) {
                    if (line.startsWith("emulator-")) {
                        String emulatorId = line.split("\\s+")[0];
                        new ProcessBuilder("cmd.exe", "/c", "adb -s " + emulatorId + " emu kill")
                                .start();
                        LoggerUtil.info("Killed emulator: " + emulatorId);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtil.error("Failed to kill emulators.");
        }
    }
}

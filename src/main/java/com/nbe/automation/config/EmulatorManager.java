package com.nbe.automation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import com.nbe.automation.core.utils.LoggerUtil;

@Component
public class EmulatorManager {

    @Autowired
    private AppProperties appProperties;

    // Store emulator UDID
    private String emulatorUdid;

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
        pb.inheritIO();

        LoggerUtil.info(
                "Starting emulator " + avdName + " on port " + port + " from " + emulatorExecutable.getAbsolutePath());
        pb.start();

        // Wait for the emulator to boot up and assign the UDID
        this.emulatorUdid = getEmulatorUdid(port);
    }

    // Retrieve the UDID for the emulator by querying adb devices
    private String getEmulatorUdid(int port) {
        try {
            Process adbProcess = new ProcessBuilder("adb", "devices").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(adbProcess.getInputStream()))) {
                List<String> devices = reader.lines().collect(Collectors.toList());

                for (String device : devices) {
                    if (device.startsWith("emulator-" + port)) {
                        // Extract the emulator UDID
                        return device.split("\\s+")[0];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtil.error("Failed to get emulator UDID: " + e.getMessage());
        }
        return null; // Return null if not found
    }

    // Get the UDID of the running emulator
    public String getEmulatorUdid() {
        return emulatorUdid;
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
                        System.out.println("Killed emulator: " + emulatorId);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to kill emulators.");
        }
    }
}

package com.nbe.automation.config;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nbe.automation.core.utils.LoggerUtil;

@Component
public class AppiumServerManager {

    public void startAppiumServer(int port, int bootstrapPort, int chromePort) {
        String command = String.format(
                "appium -p %d --base-path /wd/hub --default-capabilities \"{\\\"systemPort\\\":%d,\\\"chromeDriverPort\\\":%d}\"",
                port, bootstrapPort, chromePort);

        LoggerUtil.info(String.format("Starting Appium server on port %d...", port), this.getClass());

        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            File outputFile = new File("logs/appiumServersLog.log");
            pb.redirectOutput(outputFile);
            pb.redirectError(outputFile);
            pb.start();
            waitForAppiumServer(port, 60000);
            LoggerUtil.info("Appium server is up and running on port " + port, this.getClass());
        } catch (IOException | InterruptedException e) {
            LoggerUtil.error("Failed to start Appium on port " + port, e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    public void killAllAppiumServers() {
        try {
            String command = "for /f \"tokens=2 delims=,\" %i in ('tasklist /v /fo csv ^| findstr /i \"node.exe\" ^| findstr /i \"appium\"') do taskkill /F /PID %i";
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
    
            if (exitCode == 0) {
                LoggerUtil.info("Appium servers terminated if any were running.", this.getClass());
            } else {
                LoggerUtil.error(String.format("Failed to terminate Appium server processes. Exit code: %d", exitCode), this.getClass());
            }
        } catch (IOException | InterruptedException e) {
            LoggerUtil.error("Failed to terminate Appium server processes.", e, this.getClass());
        }
    }
    

    public boolean isAppiumServerRunning(int port) {
        try {
            URL statusUrl = new URL("http://127.0.0.1:" + port + "/wd/hub/status");
            HttpURLConnection connection = (HttpURLConnection) statusUrl.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setRequestMethod("GET");
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    public void waitForAppiumServer(int port, long timeoutMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            if (isAppiumServerRunning(port)) {
                LoggerUtil.info("Appium server on port " + port + " is responding.", this.getClass());
                return;
            }

            try {
                Thread.sleep(3000); // Poll every 3 seconds
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for Appium on port " + port, this.getClass());
                Thread.currentThread().interrupt(); // Restore interrupted status

                // Log the stack trace for debugging purposes
                LoggerUtil.error("Error occurred while waiting for Appium server on port " + port, e, this.getClass());
                throw e; // Re-throw to propagate the interruption
            }
        }

        throw new RuntimeException("Timed out waiting for Appium server on port " + port);
    }

    public void killAppiumServerByPort(String port) {
        LoggerUtil.info("Shutting down Appium server for port: " + port, this.getClass());
        try {
            String command = "powershell.exe -Command \"Get-CimInstance Win32_Process | " +
                    "Where-Object { $_.CommandLine -like '*appium*' -and $_.CommandLine -like '*--port " + port
                    + "*' } | " +
                    "ForEach-Object { Stop-Process -Id $_.ProcessId -Force }\"";

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            pb.inheritIO();
            pb.start();
            LoggerUtil.info("Appium server on port " + port + " terminated.", this.getClass());
        } catch (IOException e) {
            LoggerUtil.error("Failed to terminate Appium server on port " + port, e, this.getClass());
        }
    }
}

package com.nbe.automation.config;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nbe.automation.utils.LoggerUtil;

public class AppiumServerManager {

    public void startAppiumServer(int port, int bootstrapPort, int chromePort) {
        if(isAppiumServerRunning(port))
        {
            LoggerUtil.info(String.format("Appium server is already running on port [%s]. Skipping start.", port),
                    this.getClass());
            return;
        }
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
            int exitCode = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /IM node.exe").start().waitFor();
            if (exitCode == 0)
                LoggerUtil.info("Appium servers (node.exe) terminated successfully.", this.getClass());
            else
                LoggerUtil.error("Failed to terminate Appium server processes. Exit code: " + exitCode,
                        this.getClass());
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
                LoggerUtil.info(String.format("Appium server on port [%s] is responding.", port), this.getClass());
                return;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LoggerUtil.warn("Thread interrupted while waiting for Appium on port " + port, this.getClass());
                Thread.currentThread().interrupt();
                LoggerUtil.error("Error occurred while waiting for Appium server on port " + port, e, this.getClass());
                throw e;
            }
        }

        throw new RuntimeException(String.format("Timed out waiting for Appium server on port [%s]", port));
    }

    //not working - needs fixing
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

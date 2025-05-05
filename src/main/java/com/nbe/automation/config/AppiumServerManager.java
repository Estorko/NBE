package com.nbe.automation.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nbe.automation.core.utils.LoggerUtil;

@Component
public class AppiumServerManager {

    public void startAppiumServer(int port, int bootstrapPort, int chromePort) throws IOException {
        // Format the command with properly escaped quotes for the JSON argument
        String command = String.format("appium -p %d --base-path /wd/hub --default-capabilities \"{\\\"systemPort\\\":%d,\\\"chromeDriverPort\\\":%d}\"",
                port, bootstrapPort, chromePort);

        // Use ProcessBuilder to execute the command in cmd.exe
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
        pb.inheritIO(); // Inherit I/O to see output in console
        LoggerUtil.info(String.format("Starting Appium on port %d, systemPort=%d, chromePort=%d", port, bootstrapPort, chromePort));
        pb.start(); // Start the process
    }

    public void killAllAppiumServers() {
        try {
            // Kill Appium by checking command line of Node processes
            String command = "powershell.exe -Command \"Get-CimInstance Win32_Process | " +
                             "Where-Object { $_.CommandLine -like '*appium*' } | " +
                             "ForEach-Object { Stop-Process -Id $_.ProcessId -Force }\"";
    
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            pb.inheritIO();
            pb.start();
            System.out.println("Appium servers terminated if any were running.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to terminate Appium servers.");
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
    
}

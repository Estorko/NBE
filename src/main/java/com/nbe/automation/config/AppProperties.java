package com.nbe.automation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;

@Getter
@Component
public class AppProperties {

    @Value("${android.home.env}")
    private String androidHomeEnv;

    @Value("${appium.automationName}")
    private String automationName;

    @Value("${appium.autoGrantPermissions}")
    private String autoGrantPermissions;

    @Value("${appium.deviceName}")
    private String deviceName;

    @Value("${appium.newCommandTimeout}")
    private String newCommandTimeout;

    @Value("${appium.noReset}")
    private String noReset;

    @Value("${appium.platformName}")
    private String platformName;

    @Value("${appium.serverURL}")
    private String serverURL;

    @Value("${appium.udid}")
    private String udid;

    @Value("${emulator.names}")
    private String emulatorNames;

    @Value("${nbe.appium.appActivity}")
    private String nbeAppActivity;

    @Value("${nbe.appium.appPackage}")
    private String nbeAppPackage;

    @Value("${nbe.user.id}")
    private String nbeUserId;

    @Value("${nbe.user.password}")
    private String nbeUserPassword;

    @Value("${youtube.appium.appActivity}")
    private String youtubeAppActivity;

    @Value("${youtube.appium.appPackage}")
    private String youtubeAppPackage;

    public String getAndroidHomeEnv() {
        return androidHomeEnv;
    }

    public String getAutomationName() {
        return automationName;
    }

    public String getAutoGrantPermissions() {
        return autoGrantPermissions;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public String getNoReset() {
        return noReset;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getServerURL() {
        return serverURL;
    }

    public String getUdid() {
        return udid;
    }

    public String getEmulatorNames() {
        return emulatorNames;
    }

    public String getNbeAppActivity() {
        return nbeAppActivity;
    }

    public String getNbeAppPackage() {
        return nbeAppPackage;
    }

    public String getNbeUserId() {
        return nbeUserId;
    }

    public String getNbeUserPassword() {
        return nbeUserPassword;
    }

    public String getYoutubeAppActivity() {
        return youtubeAppActivity;
    }

    public String getYoutubeAppPackage() {
        return youtubeAppPackage;
    }

}

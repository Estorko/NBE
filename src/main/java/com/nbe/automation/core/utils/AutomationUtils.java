package com.nbe.automation.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

@Component
public class AutomationUtils {

    private final AppiumUtils appiumUtils;

    public AutomationUtils(AndroidDriver driver, AppiumUtils appiumUtils) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.appiumUtils = appiumUtils;
    }

    public String maskAccountNumber(String accountNumber) {
        char[] accountChars = accountNumber.toCharArray();
        List<Integer> digitIndices = getDigitIndices(accountChars);
        Collections.shuffle(digitIndices);

        int digitsToMask = digitIndices.size() / 2;
        for (int i = 0; i < digitsToMask; i++) {
            accountChars[digitIndices.get(i)] = 'x';
        }

        return new String(accountChars);
    }

    public List<Integer> getDigitIndices(char[] chars) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                indices.add(i);
            }
        }
        return indices;
    }

    public void clickUntilVisible(String clickId, String confirmationText, int maxTries) {
        for (int i = 0; i < maxTries; i++) {
            try {
                appiumUtils.clickByAccessibilityId(clickId);
                Thread.sleep(300);
                if (appiumUtils.isDisplayedByText(confirmationText)) {
                    LoggerUtil.debug("Click successful on attempt " + (i + 1));
                    return;
                }
            } catch (Exception e) {
                LoggerUtil.warn("Click attempt " + (i + 1) + " failed: " + e.getMessage());
            }
        }
        throw new RuntimeException("Element not visible after clicking " + maxTries + " times");
    }
}

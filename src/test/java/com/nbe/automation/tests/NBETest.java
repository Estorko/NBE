package com.nbe.automation.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.nbe.automation.core.utils.AutomationUtils;
import com.nbe.automation.core.utils.LoggerUtil;
import com.nbe.automation.pages.NBE.*;

@Disabled("Temporarily skipping NBETest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = "com.nbe.automation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NBETest {
    
    @Autowired
    private AutomationUtils automationUtils;

    @Autowired
    private AccountDetailsPage accountDetailsPage;

    @Autowired
    private AccountsPage accountsPage;

    @Autowired
    private LoginPage loginPage;

    @Test
    @Order(1)
    void login() {
        try {
            LoggerUtil.info("Starting login test");
            loginPage.login();
            LoggerUtil.info("Login test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during login: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void selectAccount() {
        try {
            LoggerUtil.info("Selecting account");
            accountsPage.navigateToAccountDetails();
            LoggerUtil.info("Account selection completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error selecting account: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void getAccountNumber() {
        try {
            LoggerUtil.info("Getting account details");
            String accountNumber = accountDetailsPage.getAccountNumber();
            String iban = accountDetailsPage.getIban();

            String maskedAccountNumber = automationUtils.maskAccountNumber(accountNumber);
            String maskedIban = automationUtils.maskAccountNumber(iban);

            LoggerUtil.info("Masked account number: " + maskedAccountNumber);
            LoggerUtil.info("Masked IBAN: " + maskedIban);
        } catch (Exception e) {
            LoggerUtil.error("Error getting account details: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void verifyScroll() {
        try {
            LoggerUtil.info("Starting scroll test");
            accountDetailsPage.scrollDown();
            Thread.sleep(1000);
            accountDetailsPage.scrollUp();
            Thread.sleep(1000);
            LoggerUtil.info("Scroll test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during scroll test: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void logOut() {
        try {
            LoggerUtil.info("Starting logout test");
            accountDetailsPage.logOut();
            LoggerUtil.info("Logout test completed successfully");
        } catch (Exception e) {
            LoggerUtil.error("Error during logout test: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

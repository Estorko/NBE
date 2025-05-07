package com.nbe.automation.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
class NBETest {

    private AutomationUtils automationUtils;
    private AccountDetailsPage accountDetailsPage;
    private AccountsPage accountsPage;
    private LoginPage loginPage;

    public void setAutomationUtils(AutomationUtils automationUtils) {
        this.automationUtils = automationUtils;
    }

    public void setAccountDetailsPage(AccountDetailsPage accountDetailsPage) {
        this.accountDetailsPage = accountDetailsPage;
    }

    public void setAccountsPage(AccountsPage accountsPage) {
        this.accountsPage = accountsPage;
    }

    public void setLoginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @Test
    @Order(1)
    void login() {
        try {
            LoggerUtil.info("Starting login test", this.getClass());
            loginPage.login();
            LoggerUtil.info("Login test completed successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error during login: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void selectAccount() {
        try {
            LoggerUtil.info("Selecting account", this.getClass());
            accountsPage.navigateToAccountDetails();
            LoggerUtil.info("Account selection completed successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error selecting account: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void getAccountNumber() {
        try {
            LoggerUtil.info("Getting account details", this.getClass());
            String accountNumber = accountDetailsPage.getAccountNumber();
            String iban = accountDetailsPage.getIban();

            String maskedAccountNumber = automationUtils.maskAccountNumber(accountNumber);
            String maskedIban = automationUtils.maskAccountNumber(iban);

            LoggerUtil.info("Masked account number: " + maskedAccountNumber, this.getClass());
            LoggerUtil.info("Masked IBAN: " + maskedIban, this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error getting account details: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void verifyScroll() {
        try {
            LoggerUtil.info("Starting scroll test", this.getClass());
            accountDetailsPage.scrollDown();
            Thread.sleep(1000);
            accountDetailsPage.scrollUp();
            Thread.sleep(1000);
            LoggerUtil.info("Scroll test completed successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error during scroll test: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    void logOut() {
        try {
            LoggerUtil.info("Starting logout test", this.getClass());
            accountDetailsPage.logOut();
            LoggerUtil.info("Logout test completed successfully", this.getClass());
        } catch (Exception e) {
            LoggerUtil.error(String.format("Error during logout test: %s", e.getMessage()), e, this.getClass());
            throw new RuntimeException(e);
        }
    }
}

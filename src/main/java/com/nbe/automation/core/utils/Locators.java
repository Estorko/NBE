package com.nbe.automation.core.utils;

/**
 * Centralized locators for the NBE mobile application.
 * All UI element locators are defined here and organized by page.
 */
public class Locators {
    // ===== Login Page Locators =====
    public static final String USER_ID_FIELD = "login_username";
    public static final String PASSWORD_FIELD = "login_password";

    // ===== Accounts Page Locators =====
    public static final String ACCOUNTS_TEXT = "Accounts";
    public static final String ACCOUNT_DROPDOWN = "CSA0";

    // ===== Account Details Page Locators =====
    public static final String ACCOUNT_DETAILS_TEXT = "Account Details";
    public static final String ACCOUNT_NUMBER_TEXT = "Account Number";
    public static final String ACCOUNT_NUMBER_VIEW = "android.view.View";
    public static final int ACCOUNT_NUMBER_PARENT_INDEX = 14;
    public static final int ACCOUNT_NUMBER_CHILD_INDEX = 3;
    public static final int IBAN_PARENT_INDEX = 18;
    public static final int IBAN_CHILD_INDEX = 3;

    // ===== Common Locators =====
    public static final String LOGOUT_INDEX_BUTTON = "index";
    public static final String LOGIN_BUTTON = "Login";
} 
package com.nbe.automation.utils;

public class Locators {
    // ToDo: move to pages to follow P-O-M model
    // ===== Youtube Locators =====
    // ===== Home Page Locators =====
    public static final String YOUTUBE_SEARCH_BUTTON = "Search";
    public static final String YOUTUBE_SEARCH_FIELD_ID = "com.google.android.youtube:id/search_edit_text";

    // ===== Search Results Page Locators =====
    public static final String YOUTUBE_SEARCH_RESULT_VIEW_CHANNEL_BUTTON = "View Channel";
    public static final String YOUTUBE_SEARCH_RESULT_RECYCLER_VIEW = "com.google.android.youtube:id/results";

    // ===== Channel Page Locators =====
    public static final String YOUTUBE_CHANNEL_VIDEOS_TAB = "Videos";
    
    /*
    * First playable video is at index 8 for emulator 'nexus_5_api_29_yt | 360 x 640 dp'
    * First playable video is at index 7 for real device
    */
    // public static final int YOUTUBE_CHANNEL_VIDEO_VIEW_INDEX = 7;
    public static final String YOUTUBE_CHANNEL_VIDEO_VIEW_CONTENT_DESC = "- play video";

    // ===== NBE Locators =====
    // ===== Login Page Locators =====
    public static final String NBE_USER_ID_FIELD = "login_username";
    public static final String NBE_PASSWORD_FIELD = "login_password";

    // ===== Accounts Page Locators =====
    public static final String NBE_ACCOUNTS_TEXT = "Accounts";
    public static final String NBE_ACCOUNT_DROPDOWN = "CSA0";

    // ===== Account Details Page Locators =====
    public static final String NBE_ACCOUNT_DETAILS_TEXT = "Account Details";
    public static final String NBE_ACCOUNT_NUMBER_TEXT = "Account Number";
    public static final String NBE_ACCOUNT_NUMBER_VIEW = "android.view.View";
    public static final int NBE_ACCOUNT_NUMBER_PARENT_INDEX = 14;
    public static final int NBE_ACCOUNT_NUMBER_CHILD_INDEX = 3;
    public static final int NBE_IBAN_PARENT_INDEX = 18;
    public static final int NBE_IBAN_CHILD_INDEX = 3;

    // ===== Common Locators =====
    public static final String NBE_LOGOUT_INDEX_BUTTON = "index";
    public static final String NBE_LOGIN_BUTTON = "Login";
    public static final String YOUTUBE_VIDEO_PLAY_VIEW_ID = "com.google.android.youtube:id/watch_while_time_bar_view_overlay";
}

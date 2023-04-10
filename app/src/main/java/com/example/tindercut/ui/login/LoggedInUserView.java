package com.example.tindercut.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
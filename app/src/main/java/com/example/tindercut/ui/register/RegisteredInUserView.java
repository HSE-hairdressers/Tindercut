package com.example.tindercut.ui.register;

/**
 * Class exposing authenticated user details to the UI.
 */
class RegisteredInUserView {
    private final String displayName;
    //... other data fields that may be accessible to the UI

    RegisteredInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
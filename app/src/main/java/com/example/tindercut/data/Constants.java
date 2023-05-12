package com.example.tindercut.data;

public class Constants {
    public final static String host = "http://79.137.206.63:8011";

    public static String getLoginURL() {
        return host + "/auth/login";
    }

    public static String getRegistrationURL() {
        return host + "/auth/registration";
    }

    public static String getProfileInfoURL(Long id) {
        return host + "/hairdresser/info/" + id.toString();
    }
    public static String getProfileEditURL(Long id) {
        return host + "/hairdresser/edit/" + id.toString();
    }
}
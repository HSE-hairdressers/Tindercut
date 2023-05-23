package com.example.tindercut.data;

/**
 * Contains list of constants for app
 */
public class Constants {
    public final static String host = "http://79.137.206.63:8011";

    public final static String loginURL = "/auth/login";

    public final static String registerURL = "/auth/registration";

    public final static String imgUrl = "/img";

    public final static String uploadUrl = "hairdresser/upload";

    public final static String profileInfoUrl = "/hairdresser/info/{id}";

    public final static String profileImgUrl = "/hairdresser/images/{id}";

    public final static String profileEditUrl = "/hairdresser/edit/{id}";

    public static String getLoginURL() {
        return host + loginURL;
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
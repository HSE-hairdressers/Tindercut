package com.example.tindercut.data;

import android.content.Context;

public class User {

    public static boolean isLoggedIn(Context c) {
        return Session.getLoginStatus(c);
    }

    public static String getName(Context c) {
        return Session.getUserName(c);
    }

    public static Long getID(Context c) {
        return Session.getUserID(c);
    }

    public static void setLogin(Context c, Long id, String name) {
        Session.setLoginStatus(c, true);
        Session.setUserID(c, id);
        Session.setUserName(c, name);
    }

    public static void logout(Context c) {
        Session.setLoginStatus(c, false);
        Session.setUserID(c, (long) -1);
        Session.setUserName(c, "");
    }
}
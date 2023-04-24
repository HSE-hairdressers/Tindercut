package com.example.tindercut.data;

import android.content.Context;

public class User {

    public static boolean isLogin(Context c) {
        return Session.getLogin(c);
    }

    public static String getName(Context c) {
        return Session.getUserName(c);
    }

    public static void setLogin(Context c, String name) {
        Session.setUserName(c, name);
    }

    public static void logout(Context c) {
        Session.setUserName(c, "");
    }
}

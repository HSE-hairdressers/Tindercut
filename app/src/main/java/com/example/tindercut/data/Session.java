package com.example.tindercut.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Session {

    static final String PREF_USER_NAME = "username";
    static final String LOGIN_STATUS = "login";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putBoolean(LOGIN_STATUS, true);
        editor.apply();
    }

    public static boolean getLogin(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(LOGIN_STATUS, false);
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
}

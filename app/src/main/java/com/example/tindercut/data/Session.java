package com.example.tindercut.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Session {

    static final String USERNAME = "username";
    static final String LOGIN_STATUS = "status";
    static final String USER_ID = "username";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoginStatus(Context ctx, Boolean loggedIn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(LOGIN_STATUS, loggedIn);
        editor.apply();
    }

    public static void setUserID(Context ctx, Long userID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(USER_ID, userID);
        editor.apply();
    }

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USERNAME, userName);
        editor.apply();
    }

    public static boolean getLoginStatus(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(LOGIN_STATUS, false);
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(USERNAME, "");
    }

    public static Long getUserID(Context ctx) {
        return getSharedPreferences(ctx).getLong(USER_ID, -1);
    }
}

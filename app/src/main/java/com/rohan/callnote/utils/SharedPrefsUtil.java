package com.rohan.callnote.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rohan.callnote.BaseCallNoteActivity;

/**
 * Created by Rohan on 18-Jan-17.
 */

public class SharedPrefsUtil {

    public static final String SHARED_PREFERENCES_NAME = "CALL_NOTE";

    public static final String USER_GOOGLE_TOKEN = "user_google_token";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";
    public static final String USER_SERVER_ID = "user_id";


    private static SharedPreferences sharedPreferences;

    static {
        sharedPreferences = BaseCallNoteActivity.getInstance().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void removeKey(String key) {
        synchronized (sharedPreferences) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public static void storeStringValue(String key, String value) {
        synchronized (sharedPreferences) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String retrieveStringValue(String key, String defValue) {
        synchronized (sharedPreferences) {
            return sharedPreferences.getString(key, defValue);
        }
    }

    public static void storeIntValue(String key, int value) {
        synchronized (sharedPreferences) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static int retrieveIntValue(String key, int defValue) {
        synchronized (sharedPreferences) {
            return sharedPreferences.getInt(key, defValue);
        }
    }

    public static void storeLongValue(String key, long value) {
        synchronized (sharedPreferences) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public static long retrieveLongValue(String key, long defValue) {
        synchronized (sharedPreferences) {
            return sharedPreferences.getLong(key, defValue);
        }
    }
}

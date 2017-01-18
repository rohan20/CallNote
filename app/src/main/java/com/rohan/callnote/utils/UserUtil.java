package com.rohan.callnote.utils;

import com.rohan.callnote.models.User;

/**
 * Created by Rohan on 18-Jan-17.
 */

public class UserUtil {

    public static boolean isUserLoggedIn() {
        return getToken() != null && getToken().length() != 0;
    }

    public static void saveUser(User user) {
        if (user != null) {
            if (user.getToken() != null && user.getToken().length() != 0) {
                SharedPrefsUtil.storeStringValue(SharedPrefsUtil.USER_GOOGLE_TOKEN, user.getToken());
            }

            if (user.getEmail() != null && user.getEmail().length() != 0) {
                SharedPrefsUtil.storeStringValue(SharedPrefsUtil.USER_EMAIL, user.getEmail());
            }


            if (user.getName() != null && user.getName().length() != 0) {
                SharedPrefsUtil.storeStringValue(SharedPrefsUtil.USER_NAME, user.getName());
            }

            if (user.getServerID() >= 0) {
                SharedPrefsUtil.storeLongValue(SharedPrefsUtil.USER_SERVER_ID, user.getServerID());
            }

        }
    }

    public static String getName() {
        return SharedPrefsUtil.retrieveStringValue(SharedPrefsUtil.USER_NAME, "");
    }

    public static String getEmail() {
        return SharedPrefsUtil.retrieveStringValue(SharedPrefsUtil.USER_EMAIL, "");
    }


    public static String getToken() {
        return SharedPrefsUtil.retrieveStringValue(SharedPrefsUtil.USER_GOOGLE_TOKEN, null);
    }

    public static long getId() {
        return SharedPrefsUtil.retrieveLongValue(SharedPrefsUtil.USER_SERVER_ID, -1);
    }


    public static void logout() {
        SharedPrefsUtil.removeKey(SharedPrefsUtil.USER_NAME);
        SharedPrefsUtil.removeKey(SharedPrefsUtil.USER_EMAIL);
        SharedPrefsUtil.removeKey(SharedPrefsUtil.USER_GOOGLE_TOKEN);
        SharedPrefsUtil.removeKey(SharedPrefsUtil.USER_SERVER_ID);
    }
}

package com.rohan.callnote2.models;

/**
 * Created by Rohan on 12-Jul-17.
 */

public class App {

    String appName;
    String appDesc;
    String appIconUrl;
    String appPlayStoreUrl;

    public App(String appName, String appDesc, String appIconUrl, String appPlayStoreUrl) {
        this.appName = appName;
        this.appDesc = appDesc;
        this.appIconUrl = appIconUrl;
        this.appPlayStoreUrl = appPlayStoreUrl;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public String getAppPlayStoreUrl() {
        return appPlayStoreUrl;
    }

//    movie roll icon - http://i.imgur.com/T5Q7izs.png
//    call note icon - http://i.imgur.com/S2eUZTi.png
//    balloon popper icon - http://i.imgur.com/w5GY0yX.png

//    https://play.google.com/store/apps/details?id=com.rohan.movieroll
//    https://play.google.com/store/apps/details?id=com.rohan.callnote2
//    https://play.google.com/store/apps/details?id=com.rohan.balloongame

}


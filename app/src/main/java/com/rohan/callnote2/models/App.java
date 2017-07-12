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

//    movie roll icon - https://drive.google.com/open?id=0B8LprY261E9IN2tmNUwxeXNodDg
//    call note icon - https://drive.google.com/open?id=0B8LprY261E9IV1VFVUZ2N3dsOVU
//    balloon popper icon - https://drive.google.com/open?id=0B8LprY261E9IU3JhQUZXczdjaGM

//    https://play.google.com/store/apps/details?id=com.rohan.movieroll
//    https://play.google.com/store/apps/details?id=com.rohan.callnote2
//    https://play.google.com/store/apps/details?id=com.rohan.balloongame

}


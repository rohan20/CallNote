package com.rohan.callnote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class User {

    @SerializedName("id")
    private int serverID;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("gtoken")
    private String token;

    public int getServerID() {
        return serverID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}

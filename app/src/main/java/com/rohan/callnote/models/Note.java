package com.rohan.callnote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class Note {

    @SerializedName("id")
    private int serverID;
    @SerializedName("contact")
    private String number;
    @SerializedName("text")
    private String text;
    @SerializedName("note_type")
    private String callType;
    @SerializedName("timestamp")
    private String timestamp;

    public int getServerID() {
        return serverID;
    }

    public String getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public String getCallType() {
        return callType;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

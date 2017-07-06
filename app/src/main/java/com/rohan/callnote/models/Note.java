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
    private String noteText;
    @SerializedName("note_type")
    private String callType;
    @SerializedName("timestamp")
    private String timestamp;

    private String email;

    public Note() {
        email = null;
    }

    public int getServerID() {
        return serverID;
    }

    public String getNumber() {
        return number;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getCallType() {
        return callType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

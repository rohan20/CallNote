package com.rohan.callnote2.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class ApiResponseDelete<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Integer status;
    @SerializedName("update")
    private String update;
    @SerializedName("error")
    private String error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setError(String error) {
        this.error = error;
    }
}

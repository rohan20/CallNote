package com.rohan.callnote.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohan on 17-Jan-17.
 */

public class ApiResponse<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Integer status;
    @SerializedName("update")
    private String update;

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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }


}

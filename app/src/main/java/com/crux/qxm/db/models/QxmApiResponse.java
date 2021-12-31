package com.crux.qxm.db.models;

import com.google.gson.annotations.SerializedName;

public class QxmApiResponse {
    @SerializedName("msg")
    private String message;

    public QxmApiResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "QxmApiResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}

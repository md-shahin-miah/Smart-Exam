package com.crux.qxm.db.models.response;

import com.google.gson.annotations.SerializedName;

public class UserAuthenticationResponseWithMessage extends UserAuthenticationResponse {

    @SerializedName("msg")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

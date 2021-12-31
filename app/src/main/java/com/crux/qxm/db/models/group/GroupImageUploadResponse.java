package com.crux.qxm.db.models.group;

import com.google.gson.annotations.SerializedName;

public class GroupImageUploadResponse {

    @SerializedName("msg")
    private String message;

    @SerializedName("imageUrl")
    private String imageUrl;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "GroupImageUploadResponse{" +
                "message='" + message + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

package com.crux.qxm.db.models.response;

import com.google.gson.annotations.SerializedName;

public class UploadedImageLink {
    @SerializedName("imagelink")
    private String imagelink;

    public UploadedImageLink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    @Override
    public String toString() {
        return "UploadedImageLink{" +
                "imagelink='" + imagelink + '\'' +
                '}';
    }
}

package com.crux.qxm.db.models.response;

import com.google.gson.annotations.SerializedName;

public class UserProfileImageResponse {

    @SerializedName("userProfileImage")
    private String userProfileImageURL;

    public String getUserProfileImageURL() {
        return userProfileImageURL;
    }

    public void setUserProfileImageURL(String userProfileImageURL) {
        this.userProfileImageURL = userProfileImageURL;
    }

    @Override
    public String toString() {
        return "UserProfileImageResponse{" +
                "userProfileImageURL='" + userProfileImageURL + '\'' +
                '}';
    }
}

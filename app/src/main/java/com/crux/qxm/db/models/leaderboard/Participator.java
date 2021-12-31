package com.crux.qxm.db.models.leaderboard;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class Participator {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("_id")
    private String id;

    @SerializedName("profileImage")
    private String profileImage;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModifiedProfileImage(){
        if(profileImage!=null){
            // as image might be from facebook or google as through social login
            if (profileImage.contains("facebook") || profileImage.contains("googleusercontent"))
                return profileImage;

            return IMAGE_SERVER_ROOT + profileImage;
        }
        return "";
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return
                "Participator{" +
                        "fullName = '" + fullName + '\'' +
                        ",_id = '" + id + '\'' +
                        ",profileImage = '" + profileImage + '\'' +
                        "}";
    }
}
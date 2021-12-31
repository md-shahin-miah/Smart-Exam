package com.crux.qxm.db.models.feed.feedDataWithShared;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class AddedBy {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("_id")
    private String id;

    @SerializedName("profileImage")
    private String profileImage;

    @SerializedName("groupShareTime")
    private String sharingTime;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        if (profileImage != null)
            return profileImage;
        return "";
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

    public String getSharingTime() {
        return sharingTime;
    }

    public void setSharingTime(String sharingTime) {
        this.sharingTime = sharingTime;
    }

    @Override
    public String toString() {
        return "AddedBy{" +
                "fullName='" + fullName + '\'' +
                ", id='" + id + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", sharingTime='" + sharingTime + '\'' +
                '}';
    }
}
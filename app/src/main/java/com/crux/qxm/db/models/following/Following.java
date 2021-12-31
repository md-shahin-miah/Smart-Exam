package com.crux.qxm.db.models.following;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class Following implements Parcelable {
    @SerializedName("_id")
    private String userId;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("profileImage")
    private String profileImage;
    public Following() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImage() {
        return profileImage;
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

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "Following{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
    }

    protected Following(Parcel in) {
        this.userId = in.readString();
        this.fullName = in.readString();
        this.profileImage = in.readString();
    }

    public static final Parcelable.Creator<Following> CREATOR = new Parcelable.Creator<Following>() {
        @Override
        public Following createFromParcel(Parcel source) {
            return new Following(source);
        }

        @Override
        public Following[] newArray(int size) {
            return new Following[size];
        }
    };
}

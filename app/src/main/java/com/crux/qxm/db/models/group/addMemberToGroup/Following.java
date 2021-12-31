
package com.crux.qxm.db.models.group.addMemberToGroup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class Following implements Parcelable {

    @SerializedName("_id")
    public String userId;
    @SerializedName("profileImage")
    public String profileImage;
    @SerializedName("fullName")
    public String fullName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Following{" +
                "userId='" + userId + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.profileImage);
        dest.writeString(this.fullName);
    }

    public Following() {
    }

    protected Following(Parcel in) {
        this.userId = in.readString();
        this.profileImage = in.readString();
        this.fullName = in.readString();
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

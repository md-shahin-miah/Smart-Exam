package com.crux.qxm.db.models.enroll.receivedEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class EnrolledUser implements Parcelable {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("_id")
    private String id;

    @SerializedName("profileImage")
    private String profileImage;

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



    @Override
    public String toString() {
        return
                "EnrolledUser{" +
                        "fullName = '" + fullName + '\'' +
                        ",_id = '" + id + '\'' +
                        ",profileImage = '" + profileImage + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.id);
        dest.writeString(this.profileImage);
    }

    public EnrolledUser() {
    }

    protected EnrolledUser(Parcel in) {
        this.fullName = in.readString();
        this.id = in.readString();
        this.profileImage = in.readString();
    }

    public static final Parcelable.Creator<EnrolledUser> CREATOR = new Parcelable.Creator<EnrolledUser>() {
        @Override
        public EnrolledUser createFromParcel(Parcel source) {
            return new EnrolledUser(source);
        }

        @Override
        public EnrolledUser[] newArray(int size) {
            return new EnrolledUser[size];
        }
    };
}
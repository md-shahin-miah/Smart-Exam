package com.crux.qxm.db.models.notification.qxmNotification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class QxmCreator implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("profileImage")
    private String profileImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "QxmCreator{" +
                "id='" + id + '\'' +
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
        dest.writeString(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
    }

    public QxmCreator() {
    }

    protected QxmCreator(Parcel in) {
        this.id = in.readString();
        this.fullName = in.readString();
        this.profileImage = in.readString();
    }

    public static final Parcelable.Creator<QxmCreator> CREATOR = new Parcelable.Creator<QxmCreator>() {
        @Override
        public QxmCreator createFromParcel(Parcel source) {
            return new QxmCreator(source);
        }

        @Override
        public QxmCreator[] newArray(int size) {
            return new QxmCreator[size];
        }
    };
}

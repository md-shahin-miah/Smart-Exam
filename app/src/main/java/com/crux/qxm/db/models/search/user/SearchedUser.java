package com.crux.qxm.db.models.search.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class SearchedUser implements Parcelable {

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
        return "SearchedUser{" +
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

    public SearchedUser() {
    }

    protected SearchedUser(Parcel in) {
        this.id = in.readString();
        this.fullName = in.readString();
        this.profileImage = in.readString();
    }

    public static final Parcelable.Creator<SearchedUser> CREATOR = new Parcelable.Creator<SearchedUser>() {
        @Override
        public SearchedUser createFromParcel(Parcel source) {
            return new SearchedUser(source);
        }

        @Override
        public SearchedUser[] newArray(int size) {
            return new SearchedUser[size];
        }
    };
}

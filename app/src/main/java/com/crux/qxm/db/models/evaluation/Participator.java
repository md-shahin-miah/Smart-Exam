
package com.crux.qxm.db.models.evaluation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class Participator implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("profileImage")
    private String profileImage;
    @SerializedName("fullName")
    private String fullName;

    public Participator() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "Participator{" +
                "id='" + id + '\'' +
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
        dest.writeString(this.id);
        dest.writeString(this.profileImage);
        dest.writeString(this.fullName);
    }

    protected Participator(Parcel in) {
        this.id = in.readString();
        this.profileImage = in.readString();
        this.fullName = in.readString();
    }

    public static final Parcelable.Creator<Participator> CREATOR = new Parcelable.Creator<Participator>() {
        @Override
        public Participator createFromParcel(Parcel source) {
            return new Participator(source);
        }

        @Override
        public Participator[] newArray(int size) {
            return new Participator[size];
        }
    };
}

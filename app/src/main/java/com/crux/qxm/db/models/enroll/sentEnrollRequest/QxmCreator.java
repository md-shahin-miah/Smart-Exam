package com.crux.qxm.db.models.enroll.sentEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QxmCreator implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("profileImage")
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "QxmCreator{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", image='" + image + '\'' +
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
        dest.writeString(this.image);
    }

    public QxmCreator() {
    }

    protected QxmCreator(Parcel in) {
        this.id = in.readString();
        this.fullName = in.readString();
        this.image = in.readString();
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

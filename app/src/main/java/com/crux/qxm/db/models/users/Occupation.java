package com.crux.qxm.db.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Occupation implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("designation")
    private String designation;
    @SerializedName("organization")
    private String organization;
    @SerializedName("workDuration")
    private String workDuration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(String workDuration) {
        this.workDuration = workDuration;
    }


    public Occupation() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.designation);
        dest.writeString(this.organization);
        dest.writeString(this.workDuration);
    }

    protected Occupation(Parcel in) {
        this.id = in.readString();
        this.designation = in.readString();
        this.organization = in.readString();
        this.workDuration = in.readString();
    }

    public static final Creator<Occupation> CREATOR = new Creator<Occupation>() {
        @Override
        public Occupation createFromParcel(Parcel source) {
            return new Occupation(source);
        }

        @Override
        public Occupation[] newArray(int size) {
            return new Occupation[size];
        }
    };
}

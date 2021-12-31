package com.crux.qxm.db.models.enroll.peopleEnrolled;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.enroll.receivedEnrollRequest.EnrolledUser;
import com.crux.qxm.db.models.enroll.sentEnrollRequest.EnrolledQxm;
import com.google.gson.annotations.SerializedName;

public class PeopleEnrolledData implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("enrolledQxmOwner")
    private String enrolledQxmOwner;
    @SerializedName("status")
    private String status;
    @SerializedName("enrolledUser")
    private EnrolledUser enrolledUser;
    @SerializedName("enrolledQxm")
    private EnrolledQxm enrolledQxm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEnrolledQxmOwner() {
        return enrolledQxmOwner;
    }

    public void setEnrolledQxmOwner(String enrolledQxmOwner) {
        this.enrolledQxmOwner = enrolledQxmOwner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EnrolledUser getEnrolledUser() {
        return enrolledUser;
    }

    public void setEnrolledUser(EnrolledUser enrolledUser) {
        this.enrolledUser = enrolledUser;
    }

    public EnrolledQxm getEnrolledQxm() {
        return enrolledQxm;
    }

    public void setEnrolledQxm(EnrolledQxm enrolledQxm) {
        this.enrolledQxm = enrolledQxm;
    }

    @Override
    public String toString() {
        return "PeopleEnrolledData{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", enrolledQxmOwner='" + enrolledQxmOwner + '\'' +
                ", status='" + status + '\'' +
                ", enrolledUser=" + enrolledUser +
                ", enrolledQxm=" + enrolledQxm +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.createdAt);
        dest.writeString(this.enrolledQxmOwner);
        dest.writeString(this.status);
        dest.writeParcelable(this.enrolledUser, flags);
        dest.writeParcelable(this.enrolledQxm, flags);
    }

    public PeopleEnrolledData() {
    }

    protected PeopleEnrolledData(Parcel in) {
        this.id = in.readString();
        this.createdAt = in.readString();
        this.enrolledQxmOwner = in.readString();
        this.status = in.readString();
        this.enrolledUser = in.readParcelable(EnrolledUser.class.getClassLoader());
        this.enrolledQxm = in.readParcelable(EnrolledQxm.class.getClassLoader());
    }

    public static final Creator<PeopleEnrolledData> CREATOR = new Creator<PeopleEnrolledData>() {
        @Override
        public PeopleEnrolledData createFromParcel(Parcel source) {
            return new PeopleEnrolledData(source);
        }

        @Override
        public PeopleEnrolledData[] newArray(int size) {
            return new PeopleEnrolledData[size];
        }
    };
}

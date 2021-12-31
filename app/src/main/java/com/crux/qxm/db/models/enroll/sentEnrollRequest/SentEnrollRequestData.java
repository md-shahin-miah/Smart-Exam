package com.crux.qxm.db.models.enroll.sentEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SentEnrollRequestData implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("enrolledQxm")
    private EnrolledQxm enrolledQxm;
    @SerializedName("createdAt")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EnrolledQxm getEnrolledQxm() {
        return enrolledQxm;
    }

    public void setEnrolledQxm(EnrolledQxm enrolledQxm) {
        this.enrolledQxm = enrolledQxm;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "EnrollRequestData{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", enrolledQxm=" + enrolledQxm +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.status);
        dest.writeParcelable(this.enrolledQxm, flags);
        dest.writeString(this.createdAt);
    }

    public SentEnrollRequestData() {
    }

    protected SentEnrollRequestData(Parcel in) {
        this.id = in.readString();
        this.status = in.readString();
        this.enrolledQxm = in.readParcelable(EnrolledQxm.class.getClassLoader());
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<SentEnrollRequestData> CREATOR = new Parcelable.Creator<SentEnrollRequestData>() {
        @Override
        public SentEnrollRequestData createFromParcel(Parcel source) {
            return new SentEnrollRequestData(source);
        }

        @Override
        public SentEnrollRequestData[] newArray(int size) {
            return new SentEnrollRequestData[size];
        }
    };
}

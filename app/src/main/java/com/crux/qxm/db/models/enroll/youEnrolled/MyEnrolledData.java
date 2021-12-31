package com.crux.qxm.db.models.enroll.youEnrolled;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.enroll.sentEnrollRequest.EnrolledQxm;
import com.google.gson.annotations.SerializedName;

public class MyEnrolledData implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("status")
    private String status;
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

    @Override
    public String toString() {
        return "MyEnrolledData{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", status='" + status + '\'' +
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
        dest.writeString(this.status);
        dest.writeParcelable(this.enrolledQxm, flags);
    }

    public MyEnrolledData() {
    }

    protected MyEnrolledData(Parcel in) {
        this.id = in.readString();
        this.createdAt = in.readString();
        this.status = in.readString();
        this.enrolledQxm = in.readParcelable(EnrolledQxm.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyEnrolledData> CREATOR = new Parcelable.Creator<MyEnrolledData>() {
        @Override
        public MyEnrolledData createFromParcel(Parcel source) {
            return new MyEnrolledData(source);
        }

        @Override
        public MyEnrolledData[] newArray(int size) {
            return new MyEnrolledData[size];
        }
    };
}

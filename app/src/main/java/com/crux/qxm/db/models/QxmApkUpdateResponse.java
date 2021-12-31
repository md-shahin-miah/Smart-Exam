package com.crux.qxm.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QxmApkUpdateResponse implements Parcelable {

    @SerializedName("latestApkVersion")
    private String latestApkVersion;
    @SerializedName("msg")
    private String msg;

    public String getLatestApkVersion() {
        return latestApkVersion;
    }

    public void setLatestApkVersion(String latestApkVersion) {
        this.latestApkVersion = latestApkVersion;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ApkUpdateResponse{" +
                "latestApkVersion='" + latestApkVersion + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latestApkVersion);
        dest.writeString(this.msg);
    }

    public QxmApkUpdateResponse() {
    }

    protected QxmApkUpdateResponse(Parcel in) {
        this.latestApkVersion = in.readString();
        this.msg = in.readString();
    }

    public static final Parcelable.Creator<QxmApkUpdateResponse> CREATOR = new Parcelable.Creator<QxmApkUpdateResponse>() {
        @Override
        public QxmApkUpdateResponse createFromParcel(Parcel source) {
            return new QxmApkUpdateResponse(source);
        }

        @Override
        public QxmApkUpdateResponse[] newArray(int size) {
            return new QxmApkUpdateResponse[size];
        }
    };
}

package com.crux.qxm.db.models.qBank;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import io.realm.RealmObject;

public class QBankQxms extends RealmObject implements Parcelable {
    private String id = UUID.randomUUID().toString();
    private String qxmId;
    private String qxmThumbnail;
    private String qxmTitle;
    private String qxmCategory;
    private String qxmDescription;
    private String qxmStatus;
    private String qxmLastEditTime;
    private String ivQBankOptions;
    private String qxmJson;

    public QBankQxms() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQxmId() {
        return qxmId;
    }

    public void setQxmId(String qxmId) {
        this.qxmId = qxmId;
    }

    public String getQxmThumbnail() {
        return qxmThumbnail;
    }

    public void setQxmThumbnail(String qxmThumbnail) {
        this.qxmThumbnail = qxmThumbnail;
    }

    public String getQxmTitle() {
        return qxmTitle;
    }

    public void setQxmTitle(String qxmTitle) {
        this.qxmTitle = qxmTitle;
    }

    public String getQxmCategory() {
        return qxmCategory;
    }

    public void setQxmCategory(String qxmCategory) {
        this.qxmCategory = qxmCategory;
    }

    public String getQxmDescription() {
        return qxmDescription;
    }

    public void setQxmDescription(String qxmDescription) {
        this.qxmDescription = qxmDescription;
    }

    public String getQxmStatus() {
        return qxmStatus;
    }

    public void setQxmStatus(String qxmStatus) {
        this.qxmStatus = qxmStatus;
    }

    public String getQxmLastEditTime() {
        return qxmLastEditTime;
    }

    public void setQxmLastEditTime(String qxmLastEditTime) {
        this.qxmLastEditTime = qxmLastEditTime;
    }

    public String getIvQBankOptions() {
        return ivQBankOptions;
    }

    public void setIvQBankOptions(String ivQBankOptions) {
        this.ivQBankOptions = ivQBankOptions;
    }

    public String getQxmJson() {
        return qxmJson;
    }

    public void setQxmJson(String qxmJson) {
        this.qxmJson = qxmJson;
    }

    @Override
    public String toString() {
        return "QBankQxms{" +
                "id='" + id + '\'' +
                ", qxmId='" + qxmId + '\'' +
                ", qxmThumbnail='" + qxmThumbnail + '\'' +
                ", qxmTitle='" + qxmTitle + '\'' +
                ", qxmCategory='" + qxmCategory + '\'' +
                ", qxmDescription='" + qxmDescription + '\'' +
                ", qxmStatus='" + qxmStatus + '\'' +
                ", qxmLastEditTime='" + qxmLastEditTime + '\'' +
                ", ivQBankOptions='" + ivQBankOptions + '\'' +
                ", qxmJson='" + qxmJson + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.qxmId);
        dest.writeString(this.qxmThumbnail);
        dest.writeString(this.qxmTitle);
        dest.writeString(this.qxmCategory);
        dest.writeString(this.qxmDescription);
        dest.writeString(this.qxmStatus);
        dest.writeString(this.qxmLastEditTime);
        dest.writeString(this.ivQBankOptions);
        dest.writeString(this.qxmJson);
    }

    protected QBankQxms(Parcel in) {
        this.id = in.readString();
        this.qxmId = in.readString();
        this.qxmThumbnail = in.readString();
        this.qxmTitle = in.readString();
        this.qxmCategory = in.readString();
        this.qxmDescription = in.readString();
        this.qxmStatus = in.readString();
        this.qxmLastEditTime = in.readString();
        this.ivQBankOptions = in.readString();
        this.qxmJson = in.readString();
    }

    public static final Creator<QBankQxms> CREATOR = new Creator<QBankQxms>() {
        @Override
        public QBankQxms createFromParcel(Parcel source) {
            return new QBankQxms(source);
        }

        @Override
        public QBankQxms[] newArray(int size) {
            return new QBankQxms[size];
        }
    };
}

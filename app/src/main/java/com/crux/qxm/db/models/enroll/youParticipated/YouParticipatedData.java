package com.crux.qxm.db.models.enroll.youParticipated;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class YouParticipatedData implements Parcelable {

    @SerializedName("feedQxmId")
    private String qxmId;
    @SerializedName("feedTitle")
    private String qxmSetTitle;
    @SerializedName("feedDescription")
    private String qxmSetDescription;
    @SerializedName("feedOwner")
    private String qxmCreatorId;
    @SerializedName("feedCreatorName")
    private String qxmCreatorName;
    @SerializedName("feedCreatorImageURL")
    private String qxmCreatorImage;
    @SerializedName("feedCreationTime")
    private String qxmCreatedAt;
    @SerializedName("feedPrivacy")
    private String qxmPrivacy;
    @SerializedName("feedParticipantsQuantity")
    private String qxmParticipatorCount;
    @SerializedName("resultPublished")
    private boolean isResultPublished;
    @SerializedName("participatedAt")
    private String participatedAt;
    @SerializedName("isContestModeOn")
    private boolean isContestModeOn;
    @SerializedName("feedViewType")
    private String feedViewType;


    public String getQxmId() {
        return qxmId;
    }

    public void setQxmId(String qxmId) {
        this.qxmId = qxmId;
    }

    public String getQxmSetTitle() {
        return qxmSetTitle;
    }

    public void setQxmSetTitle(String qxmSetTitle) {
        this.qxmSetTitle = qxmSetTitle;
    }

    public String getQxmSetDescription() {
        return qxmSetDescription;
    }

    public void setQxmSetDescription(String qxmSetDescription) {
        this.qxmSetDescription = qxmSetDescription;
    }

    public String getQxmCreatorId() {
        return qxmCreatorId;
    }

    public void setQxmCreatorId(String qxmCreatorId) {
        this.qxmCreatorId = qxmCreatorId;
    }

    public String getQxmCreatorName() {
        return qxmCreatorName;
    }

    public void setQxmCreatorName(String qxmCreatorName) {
        this.qxmCreatorName = qxmCreatorName;
    }

    public String getQxmCreatorImage() {
        return qxmCreatorImage;
    }

    public void setQxmCreatorImage(String qxmCreatorImage) {
        this.qxmCreatorImage = qxmCreatorImage;
    }

    public String getQxmCreatedAt() {
        return qxmCreatedAt;
    }

    public void setQxmCreatedAt(String qxmCreatedAt) {
        this.qxmCreatedAt = qxmCreatedAt;
    }

    public String getQxmPrivacy() {
        return qxmPrivacy;
    }

    public void setQxmPrivacy(String qxmPrivacy) {
        this.qxmPrivacy = qxmPrivacy;
    }

    public String getQxmParticipatorCount() {
        return qxmParticipatorCount;
    }

    public void setQxmParticipatorCount(String qxmParticipatorCount) {
        this.qxmParticipatorCount = qxmParticipatorCount;
    }

    public boolean isResultPublished() {
        return isResultPublished;
    }

    public void setResultPublished(boolean resultPublished) {
        isResultPublished = resultPublished;
    }

    public String getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(String participatedAt) {
        this.participatedAt = participatedAt;
    }

    public boolean isContestModeOn() {
        return isContestModeOn;
    }

    public void setContestModeOn(boolean contestModeOn) {
        isContestModeOn = contestModeOn;
    }

    public String getFeedViewType() {
        return feedViewType;
    }

    public void setFeedViewType(String feedViewType) {
        this.feedViewType = feedViewType;
    }

    @Override
    public String toString() {
        return "YouParticipatedData{" +
                "qxmId='" + qxmId + '\'' +
                ", qxmSetTitle='" + qxmSetTitle + '\'' +
                ", qxmSetDescription='" + qxmSetDescription + '\'' +
                ", qxmCreatorId='" + qxmCreatorId + '\'' +
                ", qxmCreatorName='" + qxmCreatorName + '\'' +
                ", qxmCreatorImage='" + qxmCreatorImage + '\'' +
                ", qxmCreatedAt='" + qxmCreatedAt + '\'' +
                ", qxmPrivacy='" + qxmPrivacy + '\'' +
                ", qxmParticipatorCount='" + qxmParticipatorCount + '\'' +
                ", isResultPublished=" + isResultPublished +
                ", participatedAt='" + participatedAt + '\'' +
                ", isContestModeOn=" + isContestModeOn +
                ", feedViewType='" + feedViewType + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.qxmId);
        dest.writeString(this.qxmSetTitle);
        dest.writeString(this.qxmSetDescription);
        dest.writeString(this.qxmCreatorId);
        dest.writeString(this.qxmCreatorName);
        dest.writeString(this.qxmCreatorImage);
        dest.writeString(this.qxmCreatedAt);
        dest.writeString(this.qxmPrivacy);
        dest.writeString(this.qxmParticipatorCount);
        dest.writeByte(this.isResultPublished ? (byte) 1 : (byte) 0);
        dest.writeString(this.participatedAt);
        dest.writeByte(this.isContestModeOn ? (byte) 1 : (byte) 0);
        dest.writeString(this.feedViewType);
    }

    public YouParticipatedData() {
    }

    protected YouParticipatedData(Parcel in) {
        this.qxmId = in.readString();
        this.qxmSetTitle = in.readString();
        this.qxmSetDescription = in.readString();
        this.qxmCreatorId = in.readString();
        this.qxmCreatorName = in.readString();
        this.qxmCreatorImage = in.readString();
        this.qxmCreatedAt = in.readString();
        this.qxmPrivacy = in.readString();
        this.qxmParticipatorCount = in.readString();
        this.isResultPublished = in.readByte() != 0;
        this.participatedAt = in.readString();
        this.isContestModeOn = in.readByte() != 0;
        this.feedViewType = in.readString();
    }

    public static final Creator<YouParticipatedData> CREATOR = new Creator<YouParticipatedData>() {
        @Override
        public YouParticipatedData createFromParcel(Parcel source) {
            return new YouParticipatedData(source);
        }

        @Override
        public YouParticipatedData[] newArray(int size) {
            return new YouParticipatedData[size];
        }
    };
}

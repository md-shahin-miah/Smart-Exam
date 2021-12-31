package com.crux.qxm.db.models.report;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReportModel implements Parcelable {
    private String id;
    private boolean isImageViolation;
    private boolean isContentViolation;
    private boolean isSpamming;

    @SerializedName("reportedCategory")
    private String reportedCategory;
    @SerializedName("additionalReportMsg")
    private String userComment;

    public ReportModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isImageViolation() {
        return isImageViolation;
    }

    public void setImageViolation(boolean imageViolation) {
        isImageViolation = imageViolation;
    }

    public boolean isContentViolation() {
        return isContentViolation;
    }

    public void setContentViolation(boolean contentViolation) {
        isContentViolation = contentViolation;
    }

    public boolean isSpamming() {
        return isSpamming;
    }

    public void setSpamming(boolean spamming) {
        isSpamming = spamming;
    }

    public String getReportedCategory() {
        return reportedCategory;
    }

    public void setReportedCategory(String reportedCategory) {
        this.reportedCategory = reportedCategory;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "id='" + id + '\'' +
                ", isImageViolation=" + isImageViolation +
                ", isContentViolation=" + isContentViolation +
                ", isSpamming=" + isSpamming +
                ", userComment='" + userComment + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.isImageViolation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isContentViolation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSpamming ? (byte) 1 : (byte) 0);
        dest.writeString(this.userComment);
    }

    protected ReportModel(Parcel in) {
        this.id = in.readString();
        this.isImageViolation = in.readByte() != 0;
        this.isContentViolation = in.readByte() != 0;
        this.isSpamming = in.readByte() != 0;
        this.userComment = in.readString();
    }

    public static final Creator<ReportModel> CREATOR = new Creator<ReportModel>() {
        @Override
        public ReportModel createFromParcel(Parcel source) {
            return new ReportModel(source);
        }

        @Override
        public ReportModel[] newArray(int size) {
            return new ReportModel[size];
        }
    };
}

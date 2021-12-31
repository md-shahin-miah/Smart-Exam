package com.crux.qxm.db.models.enroll.sentEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QxmSettings implements Parcelable {

    @SerializedName("questionPrivacy")
    private String questionPrivacy;

    @SerializedName("isContestModeEnabled")
    private boolean isContestModeEnabled;

    @SerializedName("qxmStartScheduleDate")
    private String qxmStartScheduleDate;


    public String getQuestionPrivacy() {
        return questionPrivacy;
    }

    public void setQuestionPrivacy(String questionPrivacy) {
        this.questionPrivacy = questionPrivacy;
    }

    public boolean isContestModeEnabled() {
        return isContestModeEnabled;
    }

    public void setContestModeEnabled(boolean contestModeEnabled) {
        isContestModeEnabled = contestModeEnabled;
    }

    public String getQxmStartScheduleDate() {
        return qxmStartScheduleDate;
    }

    public void setQxmStartScheduleDate(String qxmStartScheduleDate) {
        this.qxmStartScheduleDate = qxmStartScheduleDate;
    }

    @Override
    public String toString() {
        return "QxmAppNotificationSettings{" +
                "questionPrivacy='" + questionPrivacy + '\'' +
                ", isContestModeEnabled=" + isContestModeEnabled +
                ", qxmStartScheduleDate='" + qxmStartScheduleDate + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionPrivacy);
        dest.writeByte(this.isContestModeEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.qxmStartScheduleDate);
    }

    public QxmSettings() {
    }

    protected QxmSettings(Parcel in) {
        this.questionPrivacy = in.readString();
        this.isContestModeEnabled = in.readByte() != 0;
        this.qxmStartScheduleDate = in.readString();
    }

    public static final Creator<QxmSettings> CREATOR = new Creator<QxmSettings>() {
        @Override
        public QxmSettings createFromParcel(Parcel source) {
            return new QxmSettings(source);
        }

        @Override
        public QxmSettings[] newArray(int size) {
            return new QxmSettings[size];
        }
    };
}

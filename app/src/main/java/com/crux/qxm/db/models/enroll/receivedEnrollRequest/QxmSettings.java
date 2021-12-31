package com.crux.qxm.db.models.enroll.receivedEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QxmSettings implements Parcelable {

    @SerializedName("isContestModeEnabled")
    private boolean isContestModeEnabled;

    @SerializedName("questionPrivacy")
    private String questionPrivacy;

    public void setIsContestModeEnabled(boolean isContestModeEnabled) {
        this.isContestModeEnabled = isContestModeEnabled;
    }

    public boolean isIsContestModeEnabled() {
        return isContestModeEnabled;
    }

    public void setQuestionPrivacy(String questionPrivacy) {
        this.questionPrivacy = questionPrivacy;
    }

    public String getQuestionPrivacy() {
        return questionPrivacy;
    }

    @Override
    public String toString() {
        return
                "SingleQxmSettings{" +
                        "isContestModeEnabled = '" + isContestModeEnabled + '\'' +
                        ",questionPrivacy = '" + questionPrivacy + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isContestModeEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.questionPrivacy);
    }

    public QxmSettings() {
    }

    protected QxmSettings(Parcel in) {
        this.isContestModeEnabled = in.readByte() != 0;
        this.questionPrivacy = in.readString();
    }

    public static final Parcelable.Creator<QxmSettings> CREATOR = new Parcelable.Creator<QxmSettings>() {
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
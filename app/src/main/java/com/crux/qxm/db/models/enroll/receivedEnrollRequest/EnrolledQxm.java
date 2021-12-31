package com.crux.qxm.db.models.enroll.receivedEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EnrolledQxm implements Parcelable {

    @SerializedName("questionSet")
    private QuestionSet questionSet;

    @SerializedName("qxmSettings")
    private QxmSettings qxmSettings;

    @SerializedName("_id")
    private String id;

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
    }

    public QuestionSet getQuestionSet() {
        return questionSet;
    }

    public void setQxmSettings(QxmSettings qxmSettings) {
        this.qxmSettings = qxmSettings;
    }

    public QxmSettings getQxmSettings() {
        return qxmSettings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "EnrolledQxm{" +
                        "questionSet = '" + questionSet + '\'' +
                        ",qxmSettings = '" + qxmSettings + '\'' +
                        ",_id = '" + id + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.questionSet, flags);
        dest.writeParcelable(this.qxmSettings, flags);
        dest.writeString(this.id);
    }

    public EnrolledQxm() {
    }

    protected EnrolledQxm(Parcel in) {
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
        this.qxmSettings = in.readParcelable(QxmSettings.class.getClassLoader());
        this.id = in.readString();
    }

    public static final Parcelable.Creator<EnrolledQxm> CREATOR = new Parcelable.Creator<EnrolledQxm>() {
        @Override
        public EnrolledQxm createFromParcel(Parcel source) {
            return new EnrolledQxm(source);
        }

        @Override
        public EnrolledQxm[] newArray(int size) {
            return new EnrolledQxm[size];
        }
    };
}
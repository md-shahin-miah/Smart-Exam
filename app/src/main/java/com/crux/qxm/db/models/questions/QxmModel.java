package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.performance.UserPerformance;
import com.google.gson.annotations.SerializedName;

public class QxmModel implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("questionSet")
    private QuestionSet questionSet;
    @SerializedName("qxmSettings")
    private QuestionSettings questionSettings;
    @SerializedName("userPerformance")
    private UserPerformance userPerformance;
    @SerializedName("marks")
    private String marks;
    @SerializedName("timeTaken")
    private long timeTaken;
    @SerializedName("draftedId")
    private String draftedId;

    public QxmModel() {
    }

    public QxmModel(QuestionSet questionSet, QuestionSettings questionSettings) {
        this.questionSet = questionSet;
        this.questionSettings = questionSettings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QuestionSet getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
    }

    public QuestionSettings getQuestionSettings() {
        return questionSettings;
    }

    public void setQuestionSettings(QuestionSettings questionSettings) {
        this.questionSettings = questionSettings;
    }

    public UserPerformance getUserPerformance() {
        return userPerformance;
    }

    public void setUserPerformance(UserPerformance userPerformance) {
        this.userPerformance = userPerformance;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getDraftedId() {
        return draftedId;
    }

    public void setDraftedId(String draftedId) {
        this.draftedId = draftedId;
    }

    @Override
    public String toString() {
        return "QxmModel{" +
                "id='" + id + '\'' +
                ", questionSet=" + questionSet +
                ", questionSettings=" + questionSettings +
                ", userPerformance=" + userPerformance +
                ", marks='" + marks + '\'' +
                ", timeTaken=" + timeTaken +
                ", draftedId='" + draftedId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.questionSet, flags);
        dest.writeParcelable(this.questionSettings, flags);
        dest.writeParcelable(this.userPerformance, flags);
        dest.writeString(this.marks);
        dest.writeLong(this.timeTaken);
        dest.writeString(this.draftedId);
    }

    protected QxmModel(Parcel in) {
        this.id = in.readString();
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
        this.questionSettings = in.readParcelable(QuestionSettings.class.getClassLoader());
        this.userPerformance = in.readParcelable(UserPerformance.class.getClassLoader());
        this.marks = in.readString();
        this.timeTaken = in.readLong();
        this.draftedId = in.readString();
    }

    public static final Creator<QxmModel> CREATOR = new Creator<QxmModel>() {
        @Override
        public QxmModel createFromParcel(Parcel source) {
            return new QxmModel(source);
        }

        @Override
        public QxmModel[] newArray(int size) {
            return new QxmModel[size];
        }
    };
}

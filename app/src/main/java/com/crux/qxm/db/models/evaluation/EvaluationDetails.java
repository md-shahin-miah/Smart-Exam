package com.crux.qxm.db.models.evaluation;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.db.models.questions.QuestionSet;
import com.crux.qxm.db.models.questions.QuestionSettings;
import com.google.gson.annotations.SerializedName;

public class EvaluationDetails implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("participatedAt")
    private String participatedAt;
    @SerializedName("questionSet")
    private QuestionSet questionSet;
    @SerializedName("participatedQxm")
    private ParticipatedQxm participatedQxm;
    @SerializedName("participator")
    private Participator participator;
    @SerializedName("marks")
    private String marks;
    @SerializedName("qxmSettings")
    private QuestionSettings questionSettings;
    @SerializedName("userPerformance")
    private UserPerformance userPerformance;

    public EvaluationDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(String participatedAt) {
        this.participatedAt = participatedAt;
    }

    public QuestionSet getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
    }

    public ParticipatedQxm getParticipatedQxm() {
        return participatedQxm;
    }

    public void setParticipatedQxm(ParticipatedQxm participatedQxm) {
        this.participatedQxm = participatedQxm;
    }

    public Participator getParticipator() {
        return participator;
    }

    public void setParticipator(Participator participator) {
        this.participator = participator;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
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

    @Override
    public String toString() {
        return "EvaluationDetails{" +
                "id='" + id + '\'' +
                ", participatedAt='" + participatedAt + '\'' +
                ", questionSet=" + questionSet +
                ", participatedQxm=" + participatedQxm +
                ", participator=" + participator +
                ", marks='" + marks + '\'' +
                ", questionSettings=" + questionSettings +
                ", userPerformance=" + userPerformance +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.participatedAt);
        dest.writeParcelable(this.questionSet, flags);
        dest.writeParcelable(this.participatedQxm, flags);
        dest.writeParcelable(this.participator, flags);
        dest.writeString(this.marks);
        dest.writeParcelable(this.questionSettings, flags);
        dest.writeParcelable(this.userPerformance, flags);
    }

    protected EvaluationDetails(Parcel in) {
        this.id = in.readString();
        this.participatedAt = in.readString();
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
        this.participatedQxm = in.readParcelable(ParticipatedQxm.class.getClassLoader());
        this.participator = in.readParcelable(Participator.class.getClassLoader());
        this.marks = in.readString();
        this.questionSettings = in.readParcelable(QuestionSettings.class.getClassLoader());
        this.userPerformance = in.readParcelable(UserPerformance.class.getClassLoader());
    }

    public static final Creator<EvaluationDetails> CREATOR = new Creator<EvaluationDetails>() {
        @Override
        public EvaluationDetails createFromParcel(Parcel source) {
            return new EvaluationDetails(source);
        }

        @Override
        public EvaluationDetails[] newArray(int size) {
            return new EvaluationDetails[size];
        }
    };
}
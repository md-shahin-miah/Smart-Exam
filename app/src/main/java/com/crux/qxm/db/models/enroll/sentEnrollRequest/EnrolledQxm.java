package com.crux.qxm.db.models.enroll.sentEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EnrolledQxm implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("qxmSettings")
    private QxmSettings qxmSettings;
    @SerializedName("qxmCreatorId")
    private QxmCreator qxmCreator;
    @SerializedName("questionSet")
    private QuestionSet questionSet;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QxmSettings getQxmSettings() {
        return qxmSettings;
    }

    public void setQxmSettings(QxmSettings qxmSettings) {
        this.qxmSettings = qxmSettings;
    }

    public QxmCreator getQxmCreator() {
        return qxmCreator;
    }

    public void setQxmCreator(QxmCreator qxmCreator) {
        this.qxmCreator = qxmCreator;
    }

    public QuestionSet getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
    }

    @Override
    public String toString() {
        return "EnrolledQxm{" +
                "id='" + id + '\'' +
                ", qxmSettings=" + qxmSettings +
                ", qxmCreator=" + qxmCreator +
                ", questionSet=" + questionSet +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.qxmSettings, flags);
        dest.writeParcelable(this.qxmCreator, flags);
        dest.writeParcelable(this.questionSet, flags);
    }

    public EnrolledQxm() {
    }

    protected EnrolledQxm(Parcel in) {
        this.id = in.readString();
        this.qxmSettings = in.readParcelable(QxmSettings.class.getClassLoader());
        this.qxmCreator = in.readParcelable(QxmCreator.class.getClassLoader());
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
    }

    public static final Creator<EnrolledQxm> CREATOR = new Creator<EnrolledQxm>() {
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

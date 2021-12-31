
package com.crux.qxm.db.models.evaluation;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.questions.QuestionSet;
import com.crux.qxm.db.models.questions.QuestionSettings;
import com.google.gson.annotations.SerializedName;

public class ParticipatedQxm implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("questionSet")
    private QuestionSet questionSet;
    @SerializedName("qxmSettings")
    private QuestionSettings qxmSettings;

    public ParticipatedQxm() {
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

    public QuestionSettings getQxmSettings() {
        return qxmSettings;
    }

    public void setQxmSettings(QuestionSettings qxmSettings) {
        this.qxmSettings = qxmSettings;
    }

    @Override
    public String toString() {
        return "ParticipatedQxm{" +
                "id='" + id + '\'' +
                ", questionSet=" + questionSet +
                ", qxmSettings=" + qxmSettings +
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
        dest.writeParcelable(this.qxmSettings, flags);
    }

    protected ParticipatedQxm(Parcel in) {
        this.id = in.readString();
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
        this.qxmSettings = in.readParcelable(QuestionSettings.class.getClassLoader());
    }

    public static final Creator<ParticipatedQxm> CREATOR = new Creator<ParticipatedQxm>() {
        @Override
        public ParticipatedQxm createFromParcel(Parcel source) {
            return new ParticipatedQxm(source);
        }

        @Override
        public ParticipatedQxm[] newArray(int size) {
            return new ParticipatedQxm[size];
        }
    };
}

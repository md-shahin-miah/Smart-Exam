package com.crux.qxm.db.models.enroll.sentEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class QuestionSet implements Parcelable {

    @SerializedName("questionSetName")
    private String questionSetName;

    @SerializedName("questionSetThumbnail")
    private String questionSetThumbnail;

    @SerializedName("questionSetDescription")
    private String questionSetDescription;

    public String getQuestionSetName() {
        return questionSetName;
    }

    public void setQuestionSetName(String questionSetName) {
        this.questionSetName = questionSetName;
    }

    public String getQuestionSetThumbnail() {
        return IMAGE_SERVER_ROOT + questionSetThumbnail;
    }

    public void setQuestionSetThumbnail(String questionSetThumbnail) {
        this.questionSetThumbnail = questionSetThumbnail;
    }

    public String getQuestionSetDescription() {
        return questionSetDescription;
    }

    public void setQuestionSetDescription(String questionSetDescription) {
        this.questionSetDescription = questionSetDescription;
    }

    @Override
    public String toString() {
        return "QuestionSet{" +
                "questionSetName='" + questionSetName + '\'' +
                ", questionSetThumbnail='" + questionSetThumbnail + '\'' +
                ", questionSetDescription='" + questionSetDescription + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionSetName);
        dest.writeString(this.questionSetThumbnail);
        dest.writeString(this.questionSetDescription);
    }

    public QuestionSet() {
    }

    protected QuestionSet(Parcel in) {
        this.questionSetName = in.readString();
        this.questionSetThumbnail = in.readString();
        this.questionSetDescription = in.readString();
    }

    public static final Creator<QuestionSet> CREATOR = new Creator<QuestionSet>() {
        @Override
        public QuestionSet createFromParcel(Parcel source) {
            return new QuestionSet(source);
        }

        @Override
        public QuestionSet[] newArray(int size) {
            return new QuestionSet[size];
        }
    };
}

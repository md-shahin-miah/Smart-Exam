package com.crux.qxm.db.models.enroll.receivedEnrollRequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class QuestionSet implements Parcelable {

    @SerializedName("questionSetThumbnail")
    private String questionSetThumbnail;

    @SerializedName("questionSetName")
    private String questionSetName;

    public void setQuestionSetThumbnail(String questionSetThumbnail) {
        this.questionSetThumbnail = questionSetThumbnail;
    }

    public String getQuestionSetThumbnail() {
        if (questionSetThumbnail!= null && !questionSetThumbnail.isEmpty())
            return IMAGE_SERVER_ROOT + questionSetThumbnail;
        return "";
    }

    public void setQuestionSetName(String questionSetName) {
        this.questionSetName = questionSetName;
    }

    public String getQuestionSetName() {
        return questionSetName;
    }

    @Override
    public String toString() {
        return
                "QuestionSet{" +
                        "questionSetThumbnail = '" + questionSetThumbnail + '\'' +
                        ",questionSetName = '" + questionSetName + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionSetThumbnail);
        dest.writeString(this.questionSetName);
    }

    public QuestionSet() {
    }

    protected QuestionSet(Parcel in) {
        this.questionSetThumbnail = in.readString();
        this.questionSetName = in.readString();
    }

    public static final Parcelable.Creator<QuestionSet> CREATOR = new Parcelable.Creator<QuestionSet>() {
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
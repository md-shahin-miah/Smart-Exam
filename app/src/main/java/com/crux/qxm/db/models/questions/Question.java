
package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Question implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("questionType")
    private String questionType;
    @SerializedName("MultipleChoice")
    private MultipleChoice multipleChoice;
    @SerializedName("FillInTheBlanks")
    private FillInTheBlanks fillInTheBlanks;
    @SerializedName("ShortAnswer")
    private ShortAnswer shortAnswer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public MultipleChoice getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(MultipleChoice multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public FillInTheBlanks getFillInTheBlanks() {
        return fillInTheBlanks;
    }

    public void setFillInTheBlanks(FillInTheBlanks fillInTheBlanks) {
        this.fillInTheBlanks = fillInTheBlanks;
    }

    public ShortAnswer getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(ShortAnswer shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", questionType='" + questionType + '\'' +
                ", multipleChoice=" + multipleChoice +
                ", fillInTheBlanks=" + fillInTheBlanks +
                ", shortAnswer=" + shortAnswer +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questionType);
        dest.writeParcelable(this.multipleChoice, flags);
        dest.writeParcelable(this.fillInTheBlanks, flags);
        dest.writeParcelable(this.shortAnswer, flags);
    }

    public Question() {
    }



    protected Question(Parcel in) {
        this.id = in.readString();
        this.questionType = in.readString();
        this.multipleChoice = in.readParcelable(MultipleChoice.class.getClassLoader());
        this.fillInTheBlanks = in.readParcelable(FillInTheBlanks.class.getClassLoader());
        this.shortAnswer = in.readParcelable(ShortAnswer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}

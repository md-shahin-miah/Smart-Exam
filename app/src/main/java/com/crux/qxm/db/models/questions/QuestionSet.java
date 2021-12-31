
package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class QuestionSet implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("questionSetName")
    private String questionSetTitle;
    @SerializedName("questionSetDescription")
    private String questionSetDescription;
    @SerializedName("questionSetThumbnail")
    private String questionSetThumbnail;
    @SerializedName("questionSetYoutubeLink")
    private String questionSetYoutubeLink;
    @SerializedName("Questions")
    private List<Question> questions = null;
    @SerializedName("evaluationType")
    private String evaluationType;

    public QuestionSet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionSetTitle() {
        return questionSetTitle;
    }

    public void setQuestionSetTitle(String questionSetTitle) {
        this.questionSetTitle = questionSetTitle;
    }

    public String getQuestionSetDescription() {
        return questionSetDescription;
    }

    public void setQuestionSetDescription(String questionSetDescription) {
        this.questionSetDescription = questionSetDescription;
    }

    public String getQuestionSetThumbnail() {

        if (questionSetThumbnail != null) {
            // as image might be from facebook or google as through social login
            if (questionSetThumbnail.contains("facebook") || questionSetThumbnail.contains("googleusercontent")
                    || questionSetThumbnail.contains("i.ytimg.com"))
                return questionSetThumbnail;

            return IMAGE_SERVER_ROOT + questionSetThumbnail;
        }
        return "";
    }

    public String getUnmodifiedQuestionSetThumbnail() {
        return questionSetThumbnail;
    }

    public void setQuestionSetThumbnail(String questionSetThumbnail) {
        this.questionSetThumbnail = questionSetThumbnail;
    }

    public String getQuestionSetYoutubeLink() {
        return questionSetYoutubeLink;
    }

    public void setQuestionSetYoutubeLink(String questionSetYoutubeLink) {
        this.questionSetYoutubeLink = questionSetYoutubeLink;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    @Override
    public String toString() {
        return "QuestionSet{" +
                "id='" + id + '\'' +
                ", questionSetTitle='" + questionSetTitle + '\'' +
                ", questionSetDescription='" + questionSetDescription + '\'' +
                ", questionSetThumbnail='" + questionSetThumbnail + '\'' +
                ", questionSetYoutubeLink='" + questionSetYoutubeLink + '\'' +
                ", questions=" + questions +
                ", evaluationType='" + evaluationType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questionSetTitle);
        dest.writeString(this.questionSetDescription);
        dest.writeString(this.questionSetThumbnail);
        dest.writeString(this.questionSetYoutubeLink);
        dest.writeTypedList(this.questions);
        dest.writeString(this.evaluationType);
    }

    protected QuestionSet(Parcel in) {
        this.id = in.readString();
        this.questionSetTitle = in.readString();
        this.questionSetDescription = in.readString();
        this.questionSetThumbnail = in.readString();
        this.questionSetYoutubeLink = in.readString();
        this.questions = in.createTypedArrayList(Question.CREATOR);
        this.evaluationType = in.readString();
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

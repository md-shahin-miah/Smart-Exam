
package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_IMAGE_ROOT;

public class ShortAnswer implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("questionTitle")
    private String questionTitle;
    @SerializedName("wordLimits")
    private String wordLimits;
    @SerializedName("points")
    private String points;
    @SerializedName("image")
    private String image;
    @SerializedName("participantsAnswer")
    private String participantsAnswer;
    @SerializedName("isCorrect")
    private boolean isCorrect;
    @SerializedName("givenPoints")
    private String givenPoints;
    @SerializedName("marks")
    private String marks;
    @SerializedName("evaluatorAnswer")
    private String evaluatorAnswer;
    @SerializedName("localImage")
    private String localImage;
    @SerializedName("required")
    private Boolean required;
    @SerializedName("description")
    private String description;
    @SerializedName("hiddenDescription")
    private String hiddenDescription;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("division")
    private String division;


    public ShortAnswer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getWordLimits() {
        return wordLimits;
    }

    public void setWordLimits(String wordLimits) {
        this.wordLimits = wordLimits;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getImage() {
        if (image != null && !image.isEmpty() && !image.contains(YOUTUBE_IMAGE_ROOT))
            return IMAGE_SERVER_ROOT + image;
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParticipantsAnswer() {
        return participantsAnswer;
    }

    public void setParticipantsAnswer(String participantsAnswer) {
        this.participantsAnswer = participantsAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getGivenPoints() {
        return givenPoints;
    }

    public void setGivenPoints(String givenPoints) {
        this.givenPoints = givenPoints;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getEvaluatorAnswer() {
        return evaluatorAnswer;
    }

    public void setEvaluatorAnswer(String evaluatorAnswer) {
        this.evaluatorAnswer = evaluatorAnswer;
    }

    public String getLocalImage() {
        return localImage;
    }

    public void setLocalImage(String localImage) {
        this.localImage = localImage;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHiddenDescription() {
        return hiddenDescription;
    }

    public void setHiddenDescription(String hiddenDescription) {
        this.hiddenDescription = hiddenDescription;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String toString() {
        return "ShortAnswer{" +
                "id='" + id + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", wordLimits='" + wordLimits + '\'' +
                ", points='" + points + '\'' +
                ", image='" + image + '\'' +
                ", participantsAnswer='" + participantsAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                ", givenPoints='" + givenPoints + '\'' +
                ", marks='" + marks + '\'' +
                ", evaluatorAnswer='" + evaluatorAnswer + '\'' +
                ", localImage='" + localImage + '\'' +
                ", required=" + required +
                ", description='" + description + '\'' +
                ", hiddenDescription='" + hiddenDescription + '\'' +
                ", tags=" + tags +
                ", division='" + division + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questionTitle);
        dest.writeString(this.wordLimits);
        dest.writeString(this.points);
        dest.writeString(this.image);
        dest.writeString(this.participantsAnswer);
        dest.writeByte(this.isCorrect ? (byte) 1 : (byte) 0);
        dest.writeString(this.givenPoints);
        dest.writeString(this.marks);
        dest.writeString(this.evaluatorAnswer);
        dest.writeString(this.localImage);
        dest.writeValue(this.required);
        dest.writeString(this.description);
        dest.writeString(this.hiddenDescription);
        dest.writeStringList(this.tags);
        dest.writeString(this.division);
    }

    protected ShortAnswer(Parcel in) {
        this.id = in.readString();
        this.questionTitle = in.readString();
        this.wordLimits = in.readString();
        this.points = in.readString();
        this.image = in.readString();
        this.participantsAnswer = in.readString();
        this.isCorrect = in.readByte() != 0;
        this.givenPoints = in.readString();
        this.marks = in.readString();
        this.evaluatorAnswer = in.readString();
        this.localImage = in.readString();
        this.required = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.description = in.readString();
        this.hiddenDescription = in.readString();
        this.tags = in.createStringArrayList();
        this.division = in.readString();
    }

    public static final Creator<ShortAnswer> CREATOR = new Creator<ShortAnswer>() {
        @Override
        public ShortAnswer createFromParcel(Parcel source) {
            return new ShortAnswer(source);
        }

        @Override
        public ShortAnswer[] newArray(int size) {
            return new ShortAnswer[size];
        }
    };
}


package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_IMAGE_ROOT;

public class FillInTheBlanks implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("questionTitle")
    private String questionTitle;
    @SerializedName("correctAnswers")
    private ArrayList<String> correctAnswers = null;
    @SerializedName("participantsAnswers")
    private ArrayList<String> participantsAnswers = new ArrayList<>();
    @SerializedName("points")
    private String points;
    @SerializedName("achievedPoints")
    private String achievedPoints;
    @SerializedName("isCorrect")
    private boolean isCorrect;
    @SerializedName("image")
    private String image;
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
    @SerializedName("isAnswerPartiallyCorrect")
    private boolean isAnswerPartiallyCorrect = false;

    public FillInTheBlanks() {
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

    public ArrayList<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(ArrayList<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public ArrayList<String> getParticipantsAnswers() {
        return participantsAnswers;
    }

    public void setParticipantsAnswers(ArrayList<String> participantsAnswers) {
        this.participantsAnswers = participantsAnswers;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    protected FillInTheBlanks(Parcel in) {
        this.id = in.readString();
        this.questionTitle = in.readString();
        this.correctAnswers = in.createStringArrayList();
        this.participantsAnswers = in.createStringArrayList();
        this.points = in.readString();
        this.achievedPoints = in.readString();
        this.isCorrect = in.readByte() != 0;
        this.image = in.readString();
        this.localImage = in.readString();
        this.required = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.description = in.readString();
        this.hiddenDescription = in.readString();
        this.tags = in.createStringArrayList();
        this.division = in.readString();
        this.isAnswerPartiallyCorrect = in.readByte() != 0;
    }

    public static Creator<FillInTheBlanks> getCREATOR() {
        return CREATOR;
    }

    public String getAchievedPoints() {
        return achievedPoints;
    }

    public void setAchievedPoints(String achievedPoints) {
        this.achievedPoints = achievedPoints;
    }

    public boolean isAnswerPartiallyCorrect() {
        return isAnswerPartiallyCorrect;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getImage() {
        if (image != null && !image.isEmpty() && !image.contains(YOUTUBE_IMAGE_ROOT))
            return IMAGE_SERVER_ROOT + image;
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public void setAnswerPartiallyCorrect(boolean answerPartiallyCorrect) {
        isAnswerPartiallyCorrect = answerPartiallyCorrect;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "FillInTheBlanks{" +
                "id='" + id + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", correctAnswers=" + correctAnswers +
                ", participantsAnswers=" + participantsAnswers +
                ", points='" + points + '\'' +
                ", achievedPoints='" + achievedPoints + '\'' +
                ", isCorrect=" + isCorrect +
                ", image='" + image + '\'' +
                ", localImage='" + localImage + '\'' +
                ", required=" + required +
                ", description='" + description + '\'' +
                ", hiddenDescription='" + hiddenDescription + '\'' +
                ", tags=" + tags +
                ", division='" + division + '\'' +
                ", isAnswerPartiallyCorrect=" + isAnswerPartiallyCorrect +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questionTitle);
        dest.writeStringList(this.correctAnswers);
        dest.writeStringList(this.participantsAnswers);
        dest.writeString(this.points);
        dest.writeString(this.achievedPoints);
        dest.writeByte(this.isCorrect ? (byte) 1 : (byte) 0);
        dest.writeString(this.image);
        dest.writeString(this.localImage);
        dest.writeValue(this.required);
        dest.writeString(this.description);
        dest.writeString(this.hiddenDescription);
        dest.writeStringList(this.tags);
        dest.writeString(this.division);
        dest.writeByte(this.isAnswerPartiallyCorrect ? (byte) 1 : (byte) 0);
    }

    public static final Creator<FillInTheBlanks> CREATOR = new Creator<FillInTheBlanks>() {
        @Override
        public FillInTheBlanks createFromParcel(Parcel source) {
            return new FillInTheBlanks(source);
        }

        @Override
        public FillInTheBlanks[] newArray(int size) {
            return new FillInTheBlanks[size];
        }
    };
}

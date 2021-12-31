
package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_IMAGE_ROOT;

public class MultipleChoice implements Parcelable {

    @SerializedName("questionTitle")
    private String questionTitle;
    @SerializedName("options")
    private ArrayList<String> options;
    @SerializedName("correctAnswers")
    private ArrayList<String> correctAnswers;
    @SerializedName("participantsAnswers")
    private ArrayList<String> participantsAnswers = new ArrayList<>();
    @SerializedName("points")
    private String points;
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
    @SerializedName("youtubeVideoLink")
    private String youtubeVideoLink;

    public MultipleChoice() {
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
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

    public String getYoutubeVideoLink() {
        return youtubeVideoLink;
    }

    public void setYoutubeVideoLink(String youtubeVideoLink) {
        this.youtubeVideoLink = youtubeVideoLink;
    }

    @Override
    public String toString() {
        return "MultipleChoice{" +
                "questionTitle='" + questionTitle + '\'' +
                ", options=" + options +
                ", correctAnswers=" + correctAnswers +
                ", participantsAnswers=" + participantsAnswers +
                ", points='" + points + '\'' +
                ", isCorrect=" + isCorrect +
                ", image='" + image + '\'' +
                ", localImage='" + localImage + '\'' +
                ", required=" + required +
                ", description='" + description + '\'' +
                ", hiddenDescription='" + hiddenDescription + '\'' +
                ", tags=" + tags +
                ", division='" + division + '\'' +
                ", youtubeVideoLink='" + youtubeVideoLink + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionTitle);
        dest.writeStringList(this.options);
        dest.writeStringList(this.correctAnswers);
        dest.writeStringList(this.participantsAnswers);
        dest.writeString(this.points);
        dest.writeByte(this.isCorrect ? (byte) 1 : (byte) 0);
        dest.writeString(this.image);
        dest.writeString(this.localImage);
        dest.writeValue(this.required);
        dest.writeString(this.description);
        dest.writeString(this.hiddenDescription);
        dest.writeStringList(this.tags);
        dest.writeString(this.division);
        dest.writeString(this.youtubeVideoLink);
    }

    protected MultipleChoice(Parcel in) {
        this.questionTitle = in.readString();
        this.options = in.createStringArrayList();
        this.correctAnswers = in.createStringArrayList();
        this.participantsAnswers = in.createStringArrayList();
        this.points = in.readString();
        this.isCorrect = in.readByte() != 0;
        this.image = in.readString();
        this.localImage = in.readString();
        this.required = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.description = in.readString();
        this.hiddenDescription = in.readString();
        this.tags = in.createStringArrayList();
        this.division = in.readString();
        this.youtubeVideoLink = in.readString();
    }

    public static final Creator<MultipleChoice> CREATOR = new Creator<MultipleChoice>() {
        @Override
        public MultipleChoice createFromParcel(Parcel source) {
            return new MultipleChoice(source);
        }

        @Override
        public MultipleChoice[] newArray(int size) {
            return new MultipleChoice[size];
        }
    };
}


package com.crux.qxm.db.models.singleMCQ;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SingleMCQSettings implements Parcelable {

    @SerializedName("questionStatus")
    private String questionStatus;

    @SerializedName("questionCategory")
    private ArrayList<String> questionCategory;

    @SerializedName("qxmExpirationDate")
    private String singleMCQExpirationDate; // contestEndTime

    @SerializedName("correctAnswerVisibilityDate")
    private String correctAnswerVisibilityDate;

    // region ContestModeFields
    @SerializedName("isContestModeEnabled")
    private boolean isContestModeEnabled;

    @SerializedName("numberOfWinners")
    private int numberOfWinners;

    @SerializedName("isRaffleDrawEnabled")
    private boolean isRaffleDrawEnabled;

    @SerializedName("isParticipateAfterContestEnd")
    private boolean isParticipateAfterContestEnd;

    // endregion

    @SerializedName("leaderboardPublishDate")
    private String leaderboardPublishDate;

    @SerializedName("leaderboardPrivacy")
    private String leaderboardPrivacy;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("edited")
    private boolean edited;

    @SerializedName("lastEditedAt")
    private String lastEditedAt;

    @SerializedName("publishedAt")
    private String publishedAt;

    public SingleMCQSettings(String questionStatus, ArrayList<String> questionCategory, String singleMCQExpirationDate, String correctAnswerVisibilityDate, boolean isContestModeEnabled, int numberOfWinners, boolean isRaffleDrawEnabled, boolean isParticipateAfterContestEnd, String leaderboardPublishDate, String leaderboardPrivacy) {
        this.questionStatus = questionStatus;
        this.questionCategory = questionCategory;
        this.singleMCQExpirationDate = singleMCQExpirationDate;
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
        this.isContestModeEnabled = isContestModeEnabled;
        this.numberOfWinners = numberOfWinners;
        this.isRaffleDrawEnabled = isRaffleDrawEnabled;
        this.isParticipateAfterContestEnd = isParticipateAfterContestEnd;
        this.leaderboardPublishDate = leaderboardPublishDate;
        this.leaderboardPrivacy = leaderboardPrivacy;
    }

    public SingleMCQSettings(String questionStatus, ArrayList<String> questionCategory, String singleMCQExpirationDate, String correctAnswerVisibilityDate, boolean isContestModeEnabled, int numberOfWinners, boolean isRaffleDrawEnabled, boolean isParticipateAfterContestEnd, String leaderboardPublishDate, String leaderboardPrivacy, String createdAt, boolean edited, String lastEditedAt, String publishedAt) {
        this.questionStatus = questionStatus;
        this.questionCategory = questionCategory;
        this.singleMCQExpirationDate = singleMCQExpirationDate;
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
        this.isContestModeEnabled = isContestModeEnabled;
        this.numberOfWinners = numberOfWinners;
        this.isRaffleDrawEnabled = isRaffleDrawEnabled;
        this.isParticipateAfterContestEnd = isParticipateAfterContestEnd;
        this.leaderboardPublishDate = leaderboardPublishDate;
        this.leaderboardPrivacy = leaderboardPrivacy;
        this.createdAt = createdAt;
        this.edited = edited;
        this.lastEditedAt = lastEditedAt;
        this.publishedAt = publishedAt;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public ArrayList<String> getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(ArrayList<String> questionCategory) {
        this.questionCategory = questionCategory;
    }

    public String getSingleMCQExpirationDate() {
        return singleMCQExpirationDate;
    }

    public void setSingleMCQExpirationDate(String singleMCQExpirationDate) {
        this.singleMCQExpirationDate = singleMCQExpirationDate;
    }

    public String getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(String correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }

    public boolean isContestModeEnabled() {
        return isContestModeEnabled;
    }

    public void setContestModeEnabled(boolean contestModeEnabled) {
        isContestModeEnabled = contestModeEnabled;
    }

    public int getNumberOfWinners() {
        return numberOfWinners;
    }

    public void setNumberOfWinners(int numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }

    public boolean isRaffleDrawEnabled() {
        return isRaffleDrawEnabled;
    }

    public void setRaffleDrawEnabled(boolean raffleDrawEnabled) {
        isRaffleDrawEnabled = raffleDrawEnabled;
    }

    public boolean isParticipateAfterContestEnd() {
        return isParticipateAfterContestEnd;
    }

    public void setParticipateAfterContestEnd(boolean participateAfterContestEnd) {
        isParticipateAfterContestEnd = participateAfterContestEnd;
    }

    public String getLeaderboardPublishDate() {
        return leaderboardPublishDate;
    }

    public void setLeaderboardPublishDate(String leaderboardPublishDate) {
        this.leaderboardPublishDate = leaderboardPublishDate;
    }

    public String getLeaderboardPrivacy() {
        return leaderboardPrivacy;
    }

    public void setLeaderboardPrivacy(String leaderboardPrivacy) {
        this.leaderboardPrivacy = leaderboardPrivacy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public String getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(String lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "SingleMCQSettings{" +
                "questionStatus='" + questionStatus + '\'' +
                ", questionCategory=" + questionCategory +
                ", singleMCQExpirationDate='" + singleMCQExpirationDate + '\'' +
                ", correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", isContestModeEnabled=" + isContestModeEnabled +
                ", numberOfWinners=" + numberOfWinners +
                ", isRaffleDrawEnabled=" + isRaffleDrawEnabled +
                ", isParticipateAfterContestEnd=" + isParticipateAfterContestEnd +
                ", leaderboardPublishDate='" + leaderboardPublishDate + '\'' +
                ", leaderboardPrivacy='" + leaderboardPrivacy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", edited=" + edited +
                ", lastEditedAt='" + lastEditedAt + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionStatus);
        dest.writeStringList(this.questionCategory);
        dest.writeString(this.singleMCQExpirationDate);
        dest.writeString(this.correctAnswerVisibilityDate);
        dest.writeByte(this.isContestModeEnabled ? (byte) 1 : (byte) 0);
        dest.writeInt(this.numberOfWinners);
        dest.writeByte(this.isRaffleDrawEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isParticipateAfterContestEnd ? (byte) 1 : (byte) 0);
        dest.writeString(this.leaderboardPublishDate);
        dest.writeString(this.leaderboardPrivacy);
        dest.writeString(this.createdAt);
        dest.writeByte(this.edited ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastEditedAt);
        dest.writeString(this.publishedAt);
    }

    public SingleMCQSettings() {
    }

    protected SingleMCQSettings(Parcel in) {
        this.questionStatus = in.readString();
        this.questionCategory = in.createStringArrayList();
        this.singleMCQExpirationDate = in.readString();
        this.correctAnswerVisibilityDate = in.readString();
        this.isContestModeEnabled = in.readByte() != 0;
        this.numberOfWinners = in.readInt();
        this.isRaffleDrawEnabled = in.readByte() != 0;
        this.isParticipateAfterContestEnd = in.readByte() != 0;
        this.leaderboardPublishDate = in.readString();
        this.leaderboardPrivacy = in.readString();
        this.createdAt = in.readString();
        this.edited = in.readByte() != 0;
        this.lastEditedAt = in.readString();
        this.publishedAt = in.readString();
    }

    public static final Creator<SingleMCQSettings> CREATOR = new Creator<SingleMCQSettings>() {
        @Override
        public SingleMCQSettings createFromParcel(Parcel source) {
            return new SingleMCQSettings(source);
        }

        @Override
        public SingleMCQSettings[] newArray(int size) {
            return new SingleMCQSettings[size];
        }
    };
}

package com.crux.qxm.db.models.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.annotations.Ignore;

public class QuestionSettings implements Parcelable {

    @SerializedName("questionStatus")
    private String questionStatus;

    @Ignore
    @SerializedName("questionCategory")
    private ArrayList<String> questionCategory;

    @SerializedName("examDuration")
    private String examDuration;

    @SerializedName("questionPrivacy")
    private String questionPrivacy;

    @SerializedName("enrollStatus")
    private boolean isEnrollEnabled;

    @SerializedName("questionVisibility")
    private String questionVisibility;

    @SerializedName("qxmStartScheduleDate")
    private String qxmStartScheduleDate; //Instantly/Date-Time/Manual

    @SerializedName("qxmCurrentStatus") // Running/Not-Started/Stopped/Ended
    private String qxmCurrentStatus;

    @SerializedName("evaluationType")
    private String evaluationType; //Auto/Manual/Semi-auto

    @SerializedName("negativePoints")
    private double negativePoints;

    @SerializedName("qxmExpirationDate")
    private String qxmExpirationDate; // contestEndTime

    @SerializedName("correctAnswerVisibilityDate")
    private String correctAnswerVisibilityDate;

    // region ContestModeFields
    @SerializedName("isContestModeEnabled")
    private boolean isContestModeEnabled;

    @SerializedName("numberOfWinners")
    private int numberOfWinners;

    @SerializedName("isRaffleDrawEnabled")
    private boolean isRaffleDrawEnabled;

    @Ignore
    @SerializedName("winnerSelectionPriorityRules")
    private ArrayList<String> winnerSelectionPriorityRules = null;

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

    public QuestionSettings() {
    }

    public QuestionSettings(String questionStatus, ArrayList<String> questionCategory, String examDuration, String questionPrivacy, boolean isEnrollEnabled, String questionVisibility, String qxmStartScheduleDate, String qxmCurrentStatus, String evaluationType, double negativePoints, String qxmExpirationDate, String correctAnswerVisibilityDate, boolean isContestModeEnabled, int numberOfWinners, boolean isRaffleDrawEnabled, ArrayList<String> winnerSelectionPriorityRules, boolean isParticipateAfterContestEnd, String leaderboardPublishDate, String leaderboardPrivacy) {
        this.questionStatus = questionStatus;
        this.questionCategory = questionCategory;
        this.examDuration = examDuration;
        this.questionPrivacy = questionPrivacy;
        this.isEnrollEnabled = isEnrollEnabled;
        this.questionVisibility = questionVisibility;
        this.qxmStartScheduleDate = qxmStartScheduleDate;
        this.qxmCurrentStatus = qxmCurrentStatus;
        this.evaluationType = evaluationType;
        this.negativePoints = negativePoints;
        this.qxmExpirationDate = qxmExpirationDate;
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
        this.isContestModeEnabled = isContestModeEnabled;
        this.numberOfWinners = numberOfWinners;
        this.isRaffleDrawEnabled = isRaffleDrawEnabled;
        this.winnerSelectionPriorityRules = winnerSelectionPriorityRules;
        this.isParticipateAfterContestEnd = isParticipateAfterContestEnd;
        this.leaderboardPublishDate = leaderboardPublishDate;
        this.leaderboardPrivacy = leaderboardPrivacy;
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

    public String getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(String examDuration) {
        this.examDuration = examDuration;
    }

    public String getQuestionPrivacy() {
        return questionPrivacy;
    }

    public void setQuestionPrivacy(String questionPrivacy) {
        this.questionPrivacy = questionPrivacy;
    }

    public boolean isEnrollEnabled() {
        return isEnrollEnabled;
    }

    public void setEnrollEnabled(boolean enrollEnabled) {
        isEnrollEnabled = enrollEnabled;
    }

    public String getQuestionVisibility() {
        return questionVisibility;
    }

    public void setQuestionVisibility(String questionVisibility) {
        this.questionVisibility = questionVisibility;
    }

    public String getQxmStartScheduleDate() {
        return qxmStartScheduleDate;
    }

    public void setQxmStartScheduleDate(String qxmStartScheduleDate) {
        this.qxmStartScheduleDate = qxmStartScheduleDate;
    }

    public String getQxmCurrentStatus() {
        return qxmCurrentStatus;
    }

    public void setQxmCurrentStatus(String qxmCurrentStatus) {
        this.qxmCurrentStatus = qxmCurrentStatus;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public double getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(double negativePoints) {
        this.negativePoints = negativePoints;
    }

    public String getQxmExpirationDate() {
        return qxmExpirationDate;
    }

    public void setQxmExpirationDate(String qxmExpirationDate) {
        this.qxmExpirationDate = qxmExpirationDate;
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

    public ArrayList<String> getWinnerSelectionPriorityRules() {
        return winnerSelectionPriorityRules;
    }

    public void setWinnerSelectionPriorityRules(ArrayList<String> winnerSelectionPriorityRules) {
        this.winnerSelectionPriorityRules = winnerSelectionPriorityRules;
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
        return "QuestionSettings{" +
                "questionStatus='" + questionStatus + '\'' +
                ", questionCategory=" + questionCategory +
                ", examDuration='" + examDuration + '\'' +
                ", questionPrivacy='" + questionPrivacy + '\'' +
                ", isEnrollEnabled=" + isEnrollEnabled +
                ", questionVisibility='" + questionVisibility + '\'' +
                ", qxmStartScheduleDate='" + qxmStartScheduleDate + '\'' +
                ", qxmCurrentStatus='" + qxmCurrentStatus + '\'' +
                ", evaluationType='" + evaluationType + '\'' +
                ", negativePoints=" + negativePoints +
                ", qxmExpirationDate='" + qxmExpirationDate + '\'' +
                ", correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", isContestModeEnabled=" + isContestModeEnabled +
                ", numberOfWinners=" + numberOfWinners +
                ", isRaffleDrawEnabled=" + isRaffleDrawEnabled +
                ", winnerSelectionPriorityRules=" + winnerSelectionPriorityRules +
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
        dest.writeString(this.examDuration);
        dest.writeString(this.questionPrivacy);
        dest.writeByte(this.isEnrollEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.questionVisibility);
        dest.writeString(this.qxmStartScheduleDate);
        dest.writeString(this.qxmCurrentStatus);
        dest.writeString(this.evaluationType);
        dest.writeDouble(this.negativePoints);
        dest.writeString(this.qxmExpirationDate);
        dest.writeString(this.correctAnswerVisibilityDate);
        dest.writeByte(this.isContestModeEnabled ? (byte) 1 : (byte) 0);
        dest.writeInt(this.numberOfWinners);
        dest.writeByte(this.isRaffleDrawEnabled ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.winnerSelectionPriorityRules);
        dest.writeByte(this.isParticipateAfterContestEnd ? (byte) 1 : (byte) 0);
        dest.writeString(this.leaderboardPublishDate);
        dest.writeString(this.leaderboardPrivacy);
        dest.writeString(this.createdAt);
        dest.writeByte(this.edited ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastEditedAt);
        dest.writeString(this.publishedAt);
    }

    protected QuestionSettings(Parcel in) {
        this.questionStatus = in.readString();
        this.questionCategory = in.createStringArrayList();
        this.examDuration = in.readString();
        this.questionPrivacy = in.readString();
        this.isEnrollEnabled = in.readByte() != 0;
        this.questionVisibility = in.readString();
        this.qxmStartScheduleDate = in.readString();
        this.qxmCurrentStatus = in.readString();
        this.evaluationType = in.readString();
        this.negativePoints = in.readDouble();
        this.qxmExpirationDate = in.readString();
        this.correctAnswerVisibilityDate = in.readString();
        this.isContestModeEnabled = in.readByte() != 0;
        this.numberOfWinners = in.readInt();
        this.isRaffleDrawEnabled = in.readByte() != 0;
        this.winnerSelectionPriorityRules = in.createStringArrayList();
        this.isParticipateAfterContestEnd = in.readByte() != 0;
        this.leaderboardPublishDate = in.readString();
        this.leaderboardPrivacy = in.readString();
        this.createdAt = in.readString();
        this.edited = in.readByte() != 0;
        this.lastEditedAt = in.readString();
        this.publishedAt = in.readString();
    }

    public static final Creator<QuestionSettings> CREATOR = new Creator<QuestionSettings>() {
        @Override
        public QuestionSettings createFromParcel(Parcel source) {
            return new QuestionSettings(source);
        }

        @Override
        public QuestionSettings[] newArray(int size) {
            return new QuestionSettings[size];
        }
    };
}

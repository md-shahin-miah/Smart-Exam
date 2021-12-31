package com.crux.qxm.db.models.myQxm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleQxmSettings implements Parcelable {

	@SerializedName("leaderboardPrivacy")
	private String leaderboardPrivacy;

	@SerializedName("qxmStartScheduleDate")
	private String qxmStartScheduleDate;

	@SerializedName("edited")
	private boolean edited;

	@SerializedName("isContestModeEnabled")
	private boolean isContestModeEnabled;

	@SerializedName("correctAnswerVisibilityDate")
	private String correctAnswerVisibilityDate;

	@SerializedName("examDuration")
	private String examDuration;

	@SerializedName("winnerSelectionPriorityRules")
	private List<String> winnerSelectionPriorityRules;

	@SerializedName("questionStatus")
	private String questionStatus;

	@SerializedName("questionVisibility")
	private String questionVisibility;

	@SerializedName("evaluationType")
	private String evaluationType;

	@SerializedName("isParticipateAfterContestEnd")
	private boolean isParticipateAfterContestEnd;

	@SerializedName("negativePoints")
	private double negativePoints;

	@SerializedName("questionCategory")
	private List<String> questionCategory;

	@SerializedName("numberOfWinners")
	private int numberOfWinners;

	@SerializedName("enrollStatus")
	private boolean enrollStatus;

	@SerializedName("leaderboardPublishDate")
	private String leaderboardPublishDate;

	@SerializedName("qxmCurrentStatus")
	private String qxmCurrentStatus;

	@SerializedName("isRaffleDrawEnabled")
	private boolean isRaffleDrawEnabled;

	@SerializedName("questionPrivacy")
	private String questionPrivacy;

    @SerializedName("qxmExpirationDate")
    private String qxmExpirationDate;

    public String getLeaderboardPrivacy() {
        return leaderboardPrivacy;
    }

    public void setLeaderboardPrivacy(String leaderboardPrivacy) {
        this.leaderboardPrivacy = leaderboardPrivacy;
    }

    public String getQxmStartScheduleDate() {
        return qxmStartScheduleDate;
    }

    public void setQxmStartScheduleDate(String qxmStartScheduleDate) {
        this.qxmStartScheduleDate = qxmStartScheduleDate;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public boolean isContestModeEnabled() {
        return isContestModeEnabled;
    }

    public void setContestModeEnabled(boolean contestModeEnabled) {
        isContestModeEnabled = contestModeEnabled;
    }

    public String getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(String correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }

    public String getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(String examDuration) {
        this.examDuration = examDuration;
    }

    public List<String> getWinnerSelectionPriorityRules() {
        return winnerSelectionPriorityRules;
    }

    public void setWinnerSelectionPriorityRules(List<String> winnerSelectionPriorityRules) {
        this.winnerSelectionPriorityRules = winnerSelectionPriorityRules;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getQuestionVisibility() {
        return questionVisibility;
    }

    public void setQuestionVisibility(String questionVisibility) {
        this.questionVisibility = questionVisibility;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public boolean isParticipateAfterContestEnd() {
        return isParticipateAfterContestEnd;
    }

    public void setParticipateAfterContestEnd(boolean participateAfterContestEnd) {
        isParticipateAfterContestEnd = participateAfterContestEnd;
    }

    protected SingleQxmSettings(Parcel in) {
        this.leaderboardPrivacy = in.readString();
        this.qxmStartScheduleDate = in.readString();
        this.edited = in.readByte() != 0;
        this.isContestModeEnabled = in.readByte() != 0;
        this.correctAnswerVisibilityDate = in.readString();
        this.examDuration = in.readString();
        this.winnerSelectionPriorityRules = in.createStringArrayList();
        this.questionStatus = in.readString();
        this.questionVisibility = in.readString();
        this.evaluationType = in.readString();
        this.isParticipateAfterContestEnd = in.readByte() != 0;
        this.negativePoints = in.readDouble();
        this.questionCategory = in.createStringArrayList();
        this.numberOfWinners = in.readInt();
        this.enrollStatus = in.readByte() != 0;
        this.leaderboardPublishDate = in.readString();
        this.qxmCurrentStatus = in.readString();
        this.isRaffleDrawEnabled = in.readByte() != 0;
        this.questionPrivacy = in.readString();
        this.qxmExpirationDate = in.readString();
    }

    public double getNegativePoints() {
        return negativePoints;
    }

    public List<String> getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(List<String> questionCategory) {
        this.questionCategory = questionCategory;
    }

    public int getNumberOfWinners() {
        return numberOfWinners;
    }

    public void setNumberOfWinners(int numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }

    public boolean isEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(boolean enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public String getLeaderboardPublishDate() {
        return leaderboardPublishDate;
    }

    public void setLeaderboardPublishDate(String leaderboardPublishDate) {
        this.leaderboardPublishDate = leaderboardPublishDate;
    }

    public String getQxmCurrentStatus() {
        return qxmCurrentStatus;
    }

    public void setQxmCurrentStatus(String qxmCurrentStatus) {
        this.qxmCurrentStatus = qxmCurrentStatus;
    }

    public boolean isRaffleDrawEnabled() {
        return isRaffleDrawEnabled;
    }

    public void setRaffleDrawEnabled(boolean raffleDrawEnabled) {
        isRaffleDrawEnabled = raffleDrawEnabled;
    }

    public String getQuestionPrivacy() {
        return questionPrivacy;
    }

    public void setQuestionPrivacy(String questionPrivacy) {
        this.questionPrivacy = questionPrivacy;
    }

    public String getQxmExpirationDate() {
        return qxmExpirationDate;
    }

    public void setQxmExpirationDate(String qxmExpirationDate) {
        this.qxmExpirationDate = qxmExpirationDate;
    }

    @Override
    public String toString() {
        return "SingleQxmSettings{" +
                "leaderboardPrivacy='" + leaderboardPrivacy + '\'' +
                ", qxmStartScheduleDate='" + qxmStartScheduleDate + '\'' +
                ", edited=" + edited +
                ", isContestModeEnabled=" + isContestModeEnabled +
                ", correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", examDuration='" + examDuration + '\'' +
                ", winnerSelectionPriorityRules=" + winnerSelectionPriorityRules +
                ", questionStatus='" + questionStatus + '\'' +
                ", questionVisibility='" + questionVisibility + '\'' +
                ", evaluationType='" + evaluationType + '\'' +
                ", isParticipateAfterContestEnd=" + isParticipateAfterContestEnd +
                ", negativePoints=" + negativePoints +
                ", questionCategory=" + questionCategory +
                ", numberOfWinners=" + numberOfWinners +
                ", enrollStatus=" + enrollStatus +
                ", leaderboardPublishDate='" + leaderboardPublishDate + '\'' +
                ", qxmCurrentStatus='" + qxmCurrentStatus + '\'' +
                ", isRaffleDrawEnabled=" + isRaffleDrawEnabled +
                ", questionPrivacy='" + questionPrivacy + '\'' +
                ", qxmExpirationDate='" + qxmExpirationDate + '\'' +
                '}';
    }


    public SingleQxmSettings() {
    }

    public void setNegativePoints(double negativePoints) {
        this.negativePoints = negativePoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leaderboardPrivacy);
        dest.writeString(this.qxmStartScheduleDate);
        dest.writeByte(this.edited ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isContestModeEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.correctAnswerVisibilityDate);
        dest.writeString(this.examDuration);
        dest.writeStringList(this.winnerSelectionPriorityRules);
        dest.writeString(this.questionStatus);
        dest.writeString(this.questionVisibility);
        dest.writeString(this.evaluationType);
        dest.writeByte(this.isParticipateAfterContestEnd ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.negativePoints);
        dest.writeStringList(this.questionCategory);
        dest.writeInt(this.numberOfWinners);
        dest.writeByte(this.enrollStatus ? (byte) 1 : (byte) 0);
        dest.writeString(this.leaderboardPublishDate);
        dest.writeString(this.qxmCurrentStatus);
        dest.writeByte(this.isRaffleDrawEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.questionPrivacy);
        dest.writeString(this.qxmExpirationDate);
    }

    public static final Creator<SingleQxmSettings> CREATOR = new Creator<SingleQxmSettings>() {
        @Override
        public SingleQxmSettings createFromParcel(Parcel source) {
            return new SingleQxmSettings(source);
        }

        @Override
        public SingleQxmSettings[] newArray(int size) {
            return new SingleQxmSettings[size];
        }
    };
}
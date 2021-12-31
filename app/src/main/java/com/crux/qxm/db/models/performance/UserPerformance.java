package com.crux.qxm.db.models.performance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserPerformance implements Parcelable {

    @SerializedName("experienceLevel")
    private String experienceLevel;
    @SerializedName("participatedDate")
    private String participatedDate;
    @SerializedName("answeredPoints")
    private String answeredPoints;
    @SerializedName("timeTakenToAnswer")
    private String timeTakenToAnswer;
    @SerializedName("rankPosition")
    private String rankPosition;
    @SerializedName("achievePointsInPercentage")
    private String achievePointsInPercentage;
    @SerializedName("achievePoints")
    private String achievePoints;
    @SerializedName("totalCorrectMCQ")
    private String totalCorrectMCQ;
    @SerializedName("totalCorrectFillInTheBlanks")
    private String totalCorrectFillInTheBlanks;
    @SerializedName("totalCorrectShortAnswer")
    private String totalCorrectShortAnswer;
    @SerializedName("isAnswerCorrect")
    private boolean isAnswerCorrect;
    @SerializedName("negativePoints")
    private String negativePoints;

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getParticipatedDate() {
        return participatedDate;
    }

    public void setParticipatedDate(String participatedDate) {
        this.participatedDate = participatedDate;
    }

    public String getAnsweredPoints() {
        return answeredPoints;
    }

    public void setAnsweredPoints(String answeredPoints) {
        this.answeredPoints = answeredPoints;
    }

    public String getTimeTakenToAnswer() {
        return timeTakenToAnswer;
    }

    public void setTimeTakenToAnswer(String timeTakenToAnswer) {
        this.timeTakenToAnswer = timeTakenToAnswer;
    }

    public String getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(String rankPosition) {
        this.rankPosition = rankPosition;
    }

    public String getAchievePointsInPercentage() {
        return achievePointsInPercentage;
    }

    public void setAchievePointsInPercentage(String achievePointsInPercentage) {
        this.achievePointsInPercentage = achievePointsInPercentage;
    }

    public String getAchievePoints() {
        return achievePoints;
    }

    public void setAchievePoints(String achievePoints) {
        this.achievePoints = achievePoints;
    }

    public String getTotalCorrectMCQ() {
        return totalCorrectMCQ;
    }

    public void setTotalCorrectMCQ(String totalCorrectMCQ) {
        this.totalCorrectMCQ = totalCorrectMCQ;
    }

    public String getTotalCorrectFillInTheBlanks() {
        return totalCorrectFillInTheBlanks;
    }

    public void setTotalCorrectFillInTheBlanks(String totalCorrectFillInTheBlanks) {
        this.totalCorrectFillInTheBlanks = totalCorrectFillInTheBlanks;
    }

    public String getTotalCorrectShortAnswer() {
        return totalCorrectShortAnswer;
    }

    public void setTotalCorrectShortAnswer(String totalCorrectShortAnswer) {
        this.totalCorrectShortAnswer = totalCorrectShortAnswer;
    }

    public boolean isAnswerCorrect() {
        return isAnswerCorrect;
    }

    public void setAnswerCorrect(boolean answerCorrect) {
        isAnswerCorrect = answerCorrect;
    }

    public String getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(String negativePoints) {
        this.negativePoints = negativePoints;
    }

    @Override
    public String toString() {
        return "UserPerformance{" +
                "experienceLevel='" + experienceLevel + '\'' +
                ", participatedDate='" + participatedDate + '\'' +
                ", answeredPoints='" + answeredPoints + '\'' +
                ", timeTakenToAnswer='" + timeTakenToAnswer + '\'' +
                ", rankPosition='" + rankPosition + '\'' +
                ", achievePointsInPercentage='" + achievePointsInPercentage + '\'' +
                ", achievePoints='" + achievePoints + '\'' +
                ", totalCorrectMCQ='" + totalCorrectMCQ + '\'' +
                ", totalCorrectFillInTheBlanks='" + totalCorrectFillInTheBlanks + '\'' +
                ", totalCorrectShortAnswer='" + totalCorrectShortAnswer + '\'' +
                ", isAnswerCorrect=" + isAnswerCorrect +
                ", negativePoints='" + negativePoints + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.experienceLevel);
        dest.writeString(this.participatedDate);
        dest.writeString(this.answeredPoints);
        dest.writeString(this.timeTakenToAnswer);
        dest.writeString(this.rankPosition);
        dest.writeString(this.achievePointsInPercentage);
        dest.writeString(this.achievePoints);
        dest.writeString(this.totalCorrectMCQ);
        dest.writeString(this.totalCorrectFillInTheBlanks);
        dest.writeString(this.totalCorrectShortAnswer);
        dest.writeByte(this.isAnswerCorrect ? (byte) 1 : (byte) 0);
        dest.writeString(this.negativePoints);
    }

    public UserPerformance() {
    }

    protected UserPerformance(Parcel in) {
        this.experienceLevel = in.readString();
        this.participatedDate = in.readString();
        this.answeredPoints = in.readString();
        this.timeTakenToAnswer = in.readString();
        this.rankPosition = in.readString();
        this.achievePointsInPercentage = in.readString();
        this.achievePoints = in.readString();
        this.totalCorrectMCQ = in.readString();
        this.totalCorrectFillInTheBlanks = in.readString();
        this.totalCorrectShortAnswer = in.readString();
        this.isAnswerCorrect = in.readByte() != 0;
        this.negativePoints = in.readString();
    }

    public static final Creator<UserPerformance> CREATOR = new Creator<UserPerformance>() {
        @Override
        public UserPerformance createFromParcel(Parcel source) {
            return new UserPerformance(source);
        }

        @Override
        public UserPerformance[] newArray(int size) {
            return new UserPerformance[size];
        }
    };
}
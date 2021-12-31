package com.crux.qxm.db.models.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.db.models.questions.QuestionSet;
import com.google.gson.annotations.SerializedName;

public class FullResult implements Parcelable {
    @SerializedName("questionSet")
    private QuestionSet questionSet;

    @SerializedName("marks")
    private String marks;

    @SerializedName("evaluated")
    private boolean evaluated;

    @SerializedName("userPerformance")
    private UserPerformance userPerformance;

    @SerializedName("feedData")
    private FeedData feedData;

    @SerializedName("myPosition")
    private int myPosition;

    @SerializedName("leaderboardPublishDate")
    private String leaderboardPublishDate;

    @SerializedName("leaderboardPrivacy")
    private String leaderboardPrivacy;

    @SerializedName("qxmExpirationDate")
    private String qxmExpirationDate;

    @SerializedName(("correctAnswerVisibilityDate"))
    private String correctAnswerVisibilityDate;

    public FullResult() {
    }

    public FullResult(QuestionSet questionSet, String marks, boolean evaluated, UserPerformance userPerformance) {
        this.questionSet = questionSet;
        this.marks = marks;
        this.evaluated = evaluated;
        this.userPerformance = userPerformance;
    }

    public QuestionSet getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public UserPerformance getUserPerformance() {
        return userPerformance;
    }

    public void setUserPerformance(UserPerformance userPerformance) {
        this.userPerformance = userPerformance;
    }

    public FeedData getFeedData() {
        return feedData;
    }

    public void setFeedData(FeedData feedData) {
        this.feedData = feedData;
    }

    public int getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(int myPosition) {
        this.myPosition = myPosition;
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

    public String getQxmExpirationDate() {
        return qxmExpirationDate;
    }

    public void setQxmExpirationDate(String qxmExpirationDate) {
        this.qxmExpirationDate = qxmExpirationDate;
    }

    protected FullResult(Parcel in) {
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
        this.marks = in.readString();
        this.evaluated = in.readByte() != 0;
        this.userPerformance = in.readParcelable(UserPerformance.class.getClassLoader());
        this.feedData = in.readParcelable(FeedData.class.getClassLoader());
        this.myPosition = in.readInt();
        this.leaderboardPublishDate = in.readString();
        this.leaderboardPrivacy = in.readString();
        this.qxmExpirationDate = in.readString();
        this.correctAnswerVisibilityDate = in.readString();
    }

    public static Creator<FullResult> getCREATOR() {
        return CREATOR;
    }

    public String getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(String correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "FullResult{" +
                "questionSet=" + questionSet +
                ", marks='" + marks + '\'' +
                ", evaluated=" + evaluated +
                ", userPerformance=" + userPerformance +
                ", feedData=" + feedData +
                ", myPosition=" + myPosition +
                ", leaderboardPublishDate='" + leaderboardPublishDate + '\'' +
                ", leaderboardPrivacy='" + leaderboardPrivacy + '\'' +
                ", qxmExpirationDate='" + qxmExpirationDate + '\'' +
                ", correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.questionSet, flags);
        dest.writeString(this.marks);
        dest.writeByte(this.evaluated ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.userPerformance, flags);
        dest.writeParcelable(this.feedData, flags);
        dest.writeInt(this.myPosition);
        dest.writeString(this.leaderboardPublishDate);
        dest.writeString(this.leaderboardPrivacy);
        dest.writeString(this.qxmExpirationDate);
        dest.writeString(this.correctAnswerVisibilityDate);
    }

    public static final Creator<FullResult> CREATOR = new Creator<FullResult>() {
        @Override
        public FullResult createFromParcel(Parcel source) {
            return new FullResult(source);
        }

        @Override
        public FullResult[] newArray(int size) {
            return new FullResult[size];
        }
    };
}

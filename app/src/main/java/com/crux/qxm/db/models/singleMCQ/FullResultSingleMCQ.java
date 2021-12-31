package com.crux.qxm.db.models.singleMCQ;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.crux.qxm.db.models.questions.QuestionSet;
import com.google.gson.annotations.SerializedName;

public class FullResultSingleMCQ implements Parcelable {
    @SerializedName("questionSet")
    private MultipleChoice multipleChoice;
    @SerializedName("marks")
    private String marks;
    @SerializedName("evaluated")
    private boolean evaluated;
    @SerializedName("userPerformance")
    private UserPerformance userPerformance;
    @SerializedName("feedData")
    private FeedData feedData;

    @SerializedName("leaderboardPublishDate")
    private String leaderboardPublishDate;

    @SerializedName("leaderboardPrivacy")
    private String leaderboardPrivacy;

    @SerializedName("qxmExpirationDate")
    private String qxmExpirationDate;


    public FullResultSingleMCQ() {
    }

    public MultipleChoice getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(MultipleChoice multipleChoice) {
        this.multipleChoice = multipleChoice;
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

    @Override
    public String toString() {
        return "FullResultSingleMCQ{" +
                "multipleChoice=" + multipleChoice +
                ", marks='" + marks + '\'' +
                ", evaluated=" + evaluated +
                ", userPerformance=" + userPerformance +
                ", feedData=" + feedData +
                ", leaderboardPublishDate='" + leaderboardPublishDate + '\'' +
                ", leaderboardPrivacy='" + leaderboardPrivacy + '\'' +
                ", qxmExpirationDate='" + qxmExpirationDate + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.multipleChoice, flags);
        dest.writeString(this.marks);
        dest.writeByte(this.evaluated ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.userPerformance, flags);
        dest.writeParcelable(this.feedData, flags);
        dest.writeString(this.leaderboardPublishDate);
        dest.writeString(this.leaderboardPrivacy);
        dest.writeString(this.qxmExpirationDate);
    }

    protected FullResultSingleMCQ(Parcel in) {
        this.multipleChoice = in.readParcelable(MultipleChoice.class.getClassLoader());
        this.marks = in.readString();
        this.evaluated = in.readByte() != 0;
        this.userPerformance = in.readParcelable(UserPerformance.class.getClassLoader());
        this.feedData = in.readParcelable(FeedData.class.getClassLoader());
        this.leaderboardPublishDate = in.readString();
        this.leaderboardPrivacy = in.readString();
        this.qxmExpirationDate = in.readString();
    }

    public static final Creator<FullResultSingleMCQ> CREATOR = new Creator<FullResultSingleMCQ>() {
        @Override
        public FullResultSingleMCQ createFromParcel(Parcel source) {
            return new FullResultSingleMCQ(source);
        }

        @Override
        public FullResultSingleMCQ[] newArray(int size) {
            return new FullResultSingleMCQ[size];
        }
    };
}

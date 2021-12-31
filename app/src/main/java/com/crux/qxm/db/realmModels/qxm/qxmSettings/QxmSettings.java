package com.crux.qxm.db.realmModels.qxm.qxmSettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class QxmSettings extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    @SerializedName("questionStatus")
    private String questionStatus;

    @Ignore
    @SerializedName("questionCategory")
    private ArrayList<String> questionCategory;

    private RealmList<QxmCategory> qxmCategory;
    @SerializedName("examDurationType")
    private String examDurationType;
    @SerializedName("examDuration")
    private long examDuration;
    @SerializedName("questionPrivacy")
    private String questionPrivacy;
    @SerializedName("questionVisibility")
    private String questionVisibility;
    @SerializedName("qxmStartScheduleType")
    private String qxmStartScheduleType;
    @SerializedName("qxmStartScheduleDate")
    private long qxmStartScheduleDate;
    @SerializedName("qxmCurrentStatus")
    private String qxmCurrentStatus;
    @SerializedName("evaluationType")
    private String evaluationType;
    @SerializedName("isNegativeMarkingEnabled")
    private boolean isNegativeMarkingEnabled;
    @SerializedName("negativePoints")
    private double negativePoints;
    @SerializedName("isQxmExpirationEnabled")
    private boolean isQxmExpirationEnabled;
    @SerializedName("qxmExpirationDate")
    private long qxmExpirationDate;
    @SerializedName("correctAnswerVisibilityType")
    private String correctAnswerVisibilityType;
    @SerializedName("correctAnswerVisibilityDate")
    private long correctAnswerVisibilityDate;
    @SerializedName("createdAt")
    private long createdAt;
    @SerializedName("edited")
    private boolean edited;
    @SerializedName("lastEditedAt")
    private long lastEditedAt;
    @SerializedName("publishedAt")
    private long publishedAt;
    @SerializedName("ContestMode")
    private ContestMode contestMode;
    @SerializedName("LeaderboardSettings")
    private LeaderboardSettings leaderboardSettings;

    public QxmSettings() {
    }

    public QxmSettings(String questionStatus, ArrayList<String> questionCategory, RealmList<QxmCategory> qxmCategory, String examDurationType, long examDuration, String questionPrivacy, String questionVisibility, String qxmStartScheduleType, long qxmStartScheduleDate, String qxmCurrentStatus, String evaluationType, boolean isNegativeMarkingEnabled, double negativePoints, boolean isQxmExpirationEnabled, long qxmExpirationDate, String correctAnswerVisibilityType, long correctAnswerVisibilityDate, long createdAt, boolean edited, long lastEditedAt, long publishedAt, ContestMode contestMode, LeaderboardSettings leaderboardSettings) {
        this.questionStatus = questionStatus;
        this.questionCategory = questionCategory;
        this.qxmCategory = qxmCategory;
        this.examDurationType = examDurationType;
        this.examDuration = examDuration;
        this.questionPrivacy = questionPrivacy;
        this.questionVisibility = questionVisibility;
        this.qxmStartScheduleType = qxmStartScheduleType;
        this.qxmStartScheduleDate = qxmStartScheduleDate;
        this.qxmCurrentStatus = qxmCurrentStatus;
        this.evaluationType = evaluationType;
        this.isNegativeMarkingEnabled = isNegativeMarkingEnabled;
        this.negativePoints = negativePoints;
        this.isQxmExpirationEnabled = isQxmExpirationEnabled;
        this.qxmExpirationDate = qxmExpirationDate;
        this.correctAnswerVisibilityType = correctAnswerVisibilityType;
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
        this.createdAt = createdAt;
        this.edited = edited;
        this.lastEditedAt = lastEditedAt;
        this.publishedAt = publishedAt;
        this.contestMode = contestMode;
        this.leaderboardSettings = leaderboardSettings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public RealmList<QxmCategory> getQxmCategory() {
        return qxmCategory;
    }

    public void setQxmCategory(RealmList<QxmCategory> qxmCategory) {
        this.qxmCategory = qxmCategory;
    }

    public String getExamDurationType() {
        return examDurationType;
    }

    public void setExamDurationType(String examDurationType) {
        this.examDurationType = examDurationType;
    }

    public long getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(long examDuration) {
        this.examDuration = examDuration;
    }

    public String getQuestionPrivacy() {
        return questionPrivacy;
    }

    public void setQuestionPrivacy(String questionPrivacy) {
        this.questionPrivacy = questionPrivacy;
    }

    public String getQuestionVisibility() {
        return questionVisibility;
    }

    public void setQuestionVisibility(String questionVisibility) {
        this.questionVisibility = questionVisibility;
    }

    public String getQxmStartScheduleType() {
        return qxmStartScheduleType;
    }

    public void setQxmStartScheduleType(String qxmStartScheduleType) {
        this.qxmStartScheduleType = qxmStartScheduleType;
    }

    public long getQxmStartScheduleDate() {
        return qxmStartScheduleDate;
    }

    public void setQxmStartScheduleDate(long qxmStartScheduleDate) {
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

    public boolean isNegativeMarkingEnabled() {
        return isNegativeMarkingEnabled;
    }

    public void setNegativeMarkingEnabled(boolean negativeMarkingEnabled) {
        isNegativeMarkingEnabled = negativeMarkingEnabled;
    }

    public double getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(double negativePoints) {
        this.negativePoints = negativePoints;
    }

    public boolean isQxmExpirationEnabled() {
        return isQxmExpirationEnabled;
    }

    public void setQxmExpirationEnabled(boolean qxmExpirationEnabled) {
        isQxmExpirationEnabled = qxmExpirationEnabled;
    }

    public long getQxmExpirationDate() {
        return qxmExpirationDate;
    }

    public void setQxmExpirationDate(long qxmExpirationDate) {
        this.qxmExpirationDate = qxmExpirationDate;
    }

    public String getCorrectAnswerVisibilityType() {
        return correctAnswerVisibilityType;
    }

    public void setCorrectAnswerVisibilityType(String correctAnswerVisibilityType) {
        this.correctAnswerVisibilityType = correctAnswerVisibilityType;
    }

    public long getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(long correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public long getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(long lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    public long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(long publishedAt) {
        this.publishedAt = publishedAt;
    }

    public ContestMode getContestMode() {
        return contestMode;
    }

    public void setContestMode(ContestMode contestMode) {
        this.contestMode = contestMode;
    }

    public LeaderboardSettings getLeaderboardSettings() {
        return leaderboardSettings;
    }

    public void setLeaderboardSettings(LeaderboardSettings leaderboardSettings) {
        this.leaderboardSettings = leaderboardSettings;
    }

    @Override
    public String toString() {
        return "QxmAppNotificationSettings{" +
                "id='" + id + '\'' +
                ", questionStatus='" + questionStatus + '\'' +
                ", questionCategory=" + questionCategory +
                ", qxmCategory=" + qxmCategory +
                ", examDurationType='" + examDurationType + '\'' +
                ", examDuration=" + examDuration +
                ", questionPrivacy='" + questionPrivacy + '\'' +
                ", questionVisibility='" + questionVisibility + '\'' +
                ", qxmStartScheduleType='" + qxmStartScheduleType + '\'' +
                ", qxmStartScheduleDate=" + qxmStartScheduleDate +
                ", qxmCurrentStatus='" + qxmCurrentStatus + '\'' +
                ", evaluationType='" + evaluationType + '\'' +
                ", isNegativeMarkingEnabled=" + isNegativeMarkingEnabled +
                ", negativePoints=" + negativePoints +
                ", isQxmExpirationEnabled=" + isQxmExpirationEnabled +
                ", qxmExpirationDate=" + qxmExpirationDate +
                ", correctAnswerVisibilityType='" + correctAnswerVisibilityType + '\'' +
                ", correctAnswerVisibilityDate=" + correctAnswerVisibilityDate +
                ", createdAt=" + createdAt +
                ", edited=" + edited +
                ", lastEditedAt=" + lastEditedAt +
                ", publishedAt=" + publishedAt +
                ", contestMode=" + contestMode +
                ", leaderboardSettings=" + leaderboardSettings +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questionStatus);
        dest.writeStringList(this.questionCategory);
        dest.writeTypedList(this.qxmCategory);
        dest.writeString(this.examDurationType);
        dest.writeLong(this.examDuration);
        dest.writeString(this.questionPrivacy);
        dest.writeString(this.questionVisibility);
        dest.writeString(this.qxmStartScheduleType);
        dest.writeLong(this.qxmStartScheduleDate);
        dest.writeString(this.qxmCurrentStatus);
        dest.writeString(this.evaluationType);
        dest.writeByte(this.isNegativeMarkingEnabled ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.negativePoints);
        dest.writeByte(this.isQxmExpirationEnabled ? (byte) 1 : (byte) 0);
        dest.writeLong(this.qxmExpirationDate);
        dest.writeString(this.correctAnswerVisibilityType);
        dest.writeLong(this.correctAnswerVisibilityDate);
        dest.writeLong(this.createdAt);
        dest.writeByte(this.edited ? (byte) 1 : (byte) 0);
        dest.writeLong(this.lastEditedAt);
        dest.writeLong(this.publishedAt);
        dest.writeParcelable(this.contestMode, flags);
        dest.writeParcelable(this.leaderboardSettings, flags);
    }

    protected QxmSettings(Parcel in) {
        this.id = in.readString();
        this.questionStatus = in.readString();
        this.questionCategory = in.createStringArrayList();
        this.qxmCategory = new RealmList<>();
        this.qxmCategory.addAll(in.createTypedArrayList(QxmCategory.CREATOR));
        this.examDurationType = in.readString();
        this.examDuration = in.readLong();
        this.questionPrivacy = in.readString();
        this.questionVisibility = in.readString();
        this.qxmStartScheduleType = in.readString();
        this.qxmStartScheduleDate = in.readLong();
        this.qxmCurrentStatus = in.readString();
        this.evaluationType = in.readString();
        this.isNegativeMarkingEnabled = in.readByte() != 0;
        this.negativePoints = in.readDouble();
        this.isQxmExpirationEnabled = in.readByte() != 0;
        this.qxmExpirationDate = in.readLong();
        this.correctAnswerVisibilityType = in.readString();
        this.correctAnswerVisibilityDate = in.readLong();
        this.createdAt = in.readLong();
        this.edited = in.readByte() != 0;
        this.lastEditedAt = in.readLong();
        this.publishedAt = in.readLong();
        this.contestMode = in.readParcelable(ContestMode.class.getClassLoader());
        this.leaderboardSettings = in.readParcelable(LeaderboardSettings.class.getClassLoader());
    }

    public static final Creator<QxmSettings> CREATOR = new Creator<QxmSettings>() {
        @Override
        public QxmSettings createFromParcel(Parcel source) {
            return new QxmSettings(source);
        }

        @Override
        public QxmSettings[] newArray(int size) {
            return new QxmSettings[size];
        }
    };
}

package com.crux.qxm.db.models.userFeedback;

import com.google.gson.annotations.SerializedName;

public class UserFeedback {

    @SerializedName("content")
    private int contentScore;
    @SerializedName("design")
    private int designScore;
    @SerializedName("easyOfUse")
    private int easyOfUseScore;
    @SerializedName("overAll")
    private int overAllScore;
    @SerializedName("feedBackType")
    private String feedbackType;
    @SerializedName("feedBackMsg")
    private String feedbackMessage;
    @SerializedName("appVersion")
    private String appVersion;

    public int getContentScore() {
        return contentScore;
    }

    public void setContentScore(int contentScore) {
        this.contentScore = contentScore;
    }

    public int getDesignScore() {
        return designScore;
    }

    public void setDesignScore(int designScore) {
        this.designScore = designScore;
    }

    public int getEasyOfUseScore() {
        return easyOfUseScore;
    }

    public void setEasyOfUseScore(int easyOfUseScore) {
        this.easyOfUseScore = easyOfUseScore;
    }

    public int getOverAllScore() {
        return overAllScore;
    }

    public void setOverAllScore(int overAllScore) {
        this.overAllScore = overAllScore;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "UserFeedback{" +
                "contentScore=" + contentScore +
                ", designScore=" + designScore +
                ", easyOfUseScore=" + easyOfUseScore +
                ", overAllScore=" + overAllScore +
                ", feedbackType='" + feedbackType + '\'' +
                ", feedbackMessage='" + feedbackMessage + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}

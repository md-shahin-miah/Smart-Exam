package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

public class QxmSettings {

    @SerializedName("correctAnswerVisibilityDate")
    private String correctAnswerVisibilityDate;

    @SerializedName("questionPrivacy")
    private String questionPrivacy;

    @SerializedName(("isContestModeEnabled"))
    private boolean isContestModeEnabled;

    public String getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(String correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }

    public void setQuestionPrivacy(String questionPrivacy) {
        this.questionPrivacy = questionPrivacy;
    }

    public String getQuestionPrivacy() {
        return questionPrivacy;
    }

    public boolean isContestModeEnabled() {
        return isContestModeEnabled;
    }

    public void setContestModeEnabled(boolean contestModeEnabled) {
        isContestModeEnabled = contestModeEnabled;
    }

    @Override
    public String toString() {
        return "SingleQxmSettings{" +
                "correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", questionPrivacy='" + questionPrivacy + '\'' +
                ", isContestModeEnabled=" + isContestModeEnabled +
                '}';
    }
}
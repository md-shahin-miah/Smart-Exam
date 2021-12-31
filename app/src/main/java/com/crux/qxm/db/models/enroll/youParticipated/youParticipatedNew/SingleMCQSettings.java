package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

public class SingleMCQSettings {

    @SerializedName("correctAnswerVisibilityDate")
    private String correctAnswerVisibilityDate;

    @SerializedName(("isContestModeEnabled"))
    private boolean isContestModeEnabled;

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

    @Override
    public String toString() {
        return "SingleMCQSettings{" +
                "correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", isContestModeEnabled=" + isContestModeEnabled +
                '}';
    }
}
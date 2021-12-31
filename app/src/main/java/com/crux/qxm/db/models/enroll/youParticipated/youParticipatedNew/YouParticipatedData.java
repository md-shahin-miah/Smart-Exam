package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

public class YouParticipatedData {

    @SerializedName("participatedQxm")
    private ParticipatedQxm participatedQxm;

    @SerializedName("participatedAt")
    private String participatedAt;

    @SerializedName("_id")
    private String id;

    @SerializedName("evaluated")
    private boolean isEvaluated;

    public void setParticipatedQxm(ParticipatedQxm participatedQxm) {
        this.participatedQxm = participatedQxm;
    }

    public ParticipatedQxm getParticipatedQxm() {
        return participatedQxm;
    }

    public void setParticipatedAt(String participatedAt) {
        this.participatedAt = participatedAt;
    }

    public String getParticipatedAt() {
        return participatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }

    @Override
    public String toString() {
        return "YouParticipatedData{" +
                "participatedQxm=" + participatedQxm +
                ", participatedAt='" + participatedAt + '\'' +
                ", id='" + id + '\'' +
                ", isEvaluated=" + isEvaluated +
                '}';
    }
}
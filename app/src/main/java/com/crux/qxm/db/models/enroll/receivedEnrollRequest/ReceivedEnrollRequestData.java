package com.crux.qxm.db.models.enroll.receivedEnrollRequest;

import com.google.gson.annotations.SerializedName;

public class ReceivedEnrollRequestData {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("enrolledQxmOwner")
    private String enrolledQxmOwner;

    @SerializedName("__v")
    private int V;

    @SerializedName("_id")
    private String id;

    @SerializedName("enrolledUser")
    private EnrolledUser enrolledUser;

    @SerializedName("enrolledQxm")
    private EnrolledQxm enrolledQxm;

    @SerializedName("status")
    private String status;

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setEnrolledQxmOwner(String enrolledQxmOwner) {
        this.enrolledQxmOwner = enrolledQxmOwner;
    }

    public String getEnrolledQxmOwner() {
        return enrolledQxmOwner;
    }

    public void setV(int V) {
        this.V = V;
    }

    public int getV() {
        return V;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEnrolledUser(EnrolledUser enrolledUser) {
        this.enrolledUser = enrolledUser;
    }

    public EnrolledUser getEnrolledUser() {
        return enrolledUser;
    }

    public void setEnrolledQxm(EnrolledQxm enrolledQxm) {
        this.enrolledQxm = enrolledQxm;
    }

    public EnrolledQxm getEnrolledQxm() {
        return enrolledQxm;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "ReceivedEnrollRequestData{" +
                        "createdAt = '" + createdAt + '\'' +
                        ",enrolledQxmOwner = '" + enrolledQxmOwner + '\'' +
                        ",__v = '" + V + '\'' +
                        ",_id = '" + id + '\'' +
                        ",enrolledUser = '" + enrolledUser + '\'' +
                        ",enrolledQxm = '" + enrolledQxm + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
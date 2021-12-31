package com.crux.qxm.db.models.activityLog;

import com.google.gson.annotations.SerializedName;

public class ActivitiesItem {

    @SerializedName("type")
    private String type;
    @SerializedName("qxmId")
    private String qxmId;
    @SerializedName("qxmName")
    private String qxmName;
    @SerializedName("qxmThumbnail")
    private String qxmThumbnail;
    @SerializedName("time")
    private String time;
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupThumbnail")
    private String groupThumbnail;

    @SerializedName("newMemberId")
    private String newMemberId;
    @SerializedName("newMemberName")
    private String newMemberName;

    @SerializedName("listId")
    private String listId;
    @SerializedName("listName")
    private String listName;

    @SerializedName("followingId")
    private String followingId;
    @SerializedName("followingName")
    private String followingName;
    @SerializedName("followingThumbnail")
    private String followingThumbnail;

    @SerializedName("singleMCQId")
    private String singleMCQId;

    @SerializedName("singleMCQName")
    private String singleMCQName;

    @SerializedName("singleMCQThumbnail")
    private String singleMCQThumbnail;


    @SerializedName("singleMcqId")
    private String singleMcqId;

    @SerializedName("singleMcqName")
    private String singleMcqName;

    @SerializedName("singleMcqThumbnail")
    private String singleMcqThumbnail;


    @SerializedName("pollId")
    private String pollId;

    @SerializedName("pollName")
    private String pollTitle;

    @SerializedName("pollThumbnail")
    private String pollThumbnail;


    @SerializedName("singleQxmId")
    private String singleQxmId;

    @SerializedName("singleQxmName")
    private String singleQxmName;

    @SerializedName("singleQxmThumbnail")
    private String singleQxmThumbnail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQxmId() {
        return qxmId;
    }

    public void setQxmId(String qxmId) {
        this.qxmId = qxmId;
    }

    public String getQxmName() {
        return qxmName;
    }

    public void setQxmName(String qxmName) {
        this.qxmName = qxmName;
    }

    public String getQxmThumbnail() {
        return qxmThumbnail;
    }

    public void setQxmThumbnail(String qxmThumbnail) {
        this.qxmThumbnail = qxmThumbnail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupThumbnail() {
        return groupThumbnail;
    }

    public void setGroupThumbnail(String groupThumbnail) {
        this.groupThumbnail = groupThumbnail;
    }

    public String getNewMemberId() {
        return newMemberId;
    }

    public void setNewMemberId(String newMemberId) {
        this.newMemberId = newMemberId;
    }

    public String getNewMemberName() {
        return newMemberName;
    }

    public void setNewMemberName(String newMemberName) {
        this.newMemberName = newMemberName;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public String getFollowingName() {
        return followingName;
    }

    public void setFollowingName(String followingName) {
        this.followingName = followingName;
    }

    public String getFollowingThumbnail() {
        return followingThumbnail;
    }

    public void setFollowingThumbnail(String followingThumbnail) {
        this.followingThumbnail = followingThumbnail;
    }

    public String getSingleMCQId() {
        return singleMCQId;
    }

    public void setSingleMCQId(String singleMCQId) {
        this.singleMCQId = singleMCQId;
    }

    public String getSingleMCQName() {
        return singleMCQName;
    }

    public void setSingleMCQName(String singleMCQName) {
        this.singleMCQName = singleMCQName;
    }

    public String getSingleMCQThumbnail() {
        return singleMCQThumbnail;
    }

    public void setSingleMCQThumbnail(String singleMCQThumbnail) {
        this.singleMCQThumbnail = singleMCQThumbnail;
    }

    public String getSingleMcqId() {
        return singleMcqId;
    }

    public void setSingleMcqId(String singleMcqId) {
        this.singleMcqId = singleMcqId;
    }

    public String getSingleMcqName() {
        return singleMcqName;
    }

    public void setSingleMcqName(String singleMcqName) {
        this.singleMcqName = singleMcqName;
    }

    public String getSingleMcqThumbnail() {
        return singleMcqThumbnail;
    }

    public void setSingleMcqThumbnail(String singleMcqThumbnail) {
        this.singleMcqThumbnail = singleMcqThumbnail;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public String getPollThumbnail() {
        return pollThumbnail;
    }

    public void setPollThumbnail(String pollThumbnail) {
        this.pollThumbnail = pollThumbnail;
    }

    public String getSingleQxmId() {
        return singleQxmId;
    }

    public void setSingleQxmId(String singleQxmId) {
        this.singleQxmId = singleQxmId;
    }

    public String getSingleQxmName() {
        return singleQxmName;
    }

    public void setSingleQxmName(String singleQxmName) {
        this.singleQxmName = singleQxmName;
    }

    public String getSingleQxmThumbnail() {
        return singleQxmThumbnail;
    }

    public void setSingleQxmThumbnail(String singleQxmThumbnail) {
        this.singleQxmThumbnail = singleQxmThumbnail;
    }

    @Override
    public String toString() {
        return "ActivitiesItem{" +
                "type='" + type + '\'' +
                ", qxmId='" + qxmId + '\'' +
                ", qxmName='" + qxmName + '\'' +
                ", qxmThumbnail='" + qxmThumbnail + '\'' +
                ", time='" + time + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupThumbnail='" + groupThumbnail + '\'' +
                ", newMemberId='" + newMemberId + '\'' +
                ", newMemberName='" + newMemberName + '\'' +
                ", listId='" + listId + '\'' +
                ", listName='" + listName + '\'' +
                ", followingId='" + followingId + '\'' +
                ", followingName='" + followingName + '\'' +
                ", followingThumbnail='" + followingThumbnail + '\'' +
                ", singleMCQId='" + singleMCQId + '\'' +
                ", singleMCQName='" + singleMCQName + '\'' +
                ", singleMCQThumbnail='" + singleMCQThumbnail + '\'' +
                ", singleMcqId='" + singleMcqId + '\'' +
                ", singleMcqName='" + singleMcqName + '\'' +
                ", singleMcqThumbnail='" + singleMcqThumbnail + '\'' +
                ", pollId='" + pollId + '\'' +
                ", pollTitle='" + pollTitle + '\'' +
                ", pollThumbnail='" + pollThumbnail + '\'' +
                ", singleQxmId='" + singleQxmId + '\'' +
                ", singleQxmName='" + singleQxmName + '\'' +
                ", singleQxmThumbnail='" + singleQxmThumbnail + '\'' +
                '}';
    }
}
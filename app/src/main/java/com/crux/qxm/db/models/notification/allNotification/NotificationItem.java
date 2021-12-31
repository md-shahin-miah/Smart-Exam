package com.crux.qxm.db.models.notification.allNotification;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class NotificationItem {

    @SerializedName("pollName")
    private String pollName;

    @SerializedName("pollCreator")
    private String pollCreator;

    @SerializedName("pollId")
    private String pollId;

    @SerializedName("time")
    private String time;

    @SerializedName("userId")
    private String userId;

    @SerializedName("status")
    private String status;

    @SerializedName("qxmCreator")
    private String qxmCreator;

    @SerializedName("qxmId")
    private String qxmId;

    @SerializedName("qxmName")
    private String qxmName;

    @SerializedName("participatorId")
    private String participatorId;

    @SerializedName("participatorName")
    private String participatorName;

    @SerializedName("followerName")
    private String followerName;

    @SerializedName("followingName")
    private String followingName;

    @SerializedName("followingId")
    private String followingId;

    @SerializedName("followerId")
    private String followerId;

    @SerializedName("enrollUserName")
    private String enrollUserName;

    @SerializedName("singleMCQName")
    private String singleMCQName;

    @SerializedName("singleMCQCreator")
    private String singleMCQCreator;

    @SerializedName("singleMCQId")
    private String singleMCQId;

    @SerializedName("singleQxmCreator")
    private String singleQxmCreator;

    @SerializedName("singleQxmName")
    private String singleQxmName;

    @SerializedName("singleQxmId")
    private String singleQxmId;

    @SerializedName("adminName")
    private String adminName;

    @SerializedName("groupName")
    private String groupName;

    @SerializedName("groupId")
    private String groupId;

    @SerializedName("adminId")
    private String adminId;

    @SerializedName("newMemberName")
    private String newMemberName;

    @SerializedName("newMemberId")
    private String newMemberId;

    @SerializedName("qxmCreatorId")
    private String qxmCreatorId;

    @SerializedName("qxmCreatorName")
    private String qxmCreatorName;

    @SerializedName("userImage")
    private String userImage;

    @SerializedName("followerImage")
    private String profileImage;

    @SerializedName("groupImage")
    private String groupImage;

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public String getPollCreator() {
        return pollCreator;
    }

    public void setPollCreator(String pollCreator) {
        this.pollCreator = pollCreator;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQxmCreator() {
        return qxmCreator;
    }

    public void setQxmCreator(String qxmCreator) {
        this.qxmCreator = qxmCreator;
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

    public String getParticipatorId() {
        return participatorId;
    }

    public void setParticipatorId(String participatorId) {
        this.participatorId = participatorId;
    }

    public String getParticipatorName() {
        return participatorName;
    }

    public void setParticipatorName(String participatorName) {
        this.participatorName = participatorName;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowingName() {
        return followingName;
    }

    public void setFollowingName(String followingName) {
        this.followingName = followingName;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getEnrollUserName() {
        return enrollUserName;
    }

    public void setEnrollUserName(String enrollUserName) {
        this.enrollUserName = enrollUserName;
    }

    public String getSingleMCQName() {
        return singleMCQName;
    }

    public void setSingleMCQName(String singleMCQName) {
        this.singleMCQName = singleMCQName;
    }

    public String getSingleMCQCreator() {
        return singleMCQCreator;
    }

    public void setSingleMCQCreator(String singleMCQCreator) {
        this.singleMCQCreator = singleMCQCreator;
    }

    public String getSingleMCQId() {
        return singleMCQId;
    }

    public void setSingleMCQId(String singleMCQId) {
        this.singleMCQId = singleMCQId;
    }

    public String getSingleQxmCreator() {
        return singleQxmCreator;
    }

    public void setSingleQxmCreator(String singleQxmCreator) {
        this.singleQxmCreator = singleQxmCreator;
    }

    public String getSingleQxmName() {
        return singleQxmName;
    }

    public void setSingleQxmName(String singleQxmName) {
        this.singleQxmName = singleQxmName;
    }

    public String getSingleQxmId() {
        return singleQxmId;
    }

    public void setSingleQxmId(String singleQxmId) {
        this.singleQxmId = singleQxmId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getNewMemberName() {
        return newMemberName;
    }

    public void setNewMemberName(String newMemberName) {
        this.newMemberName = newMemberName;
    }

    public String getNewMemberId() {
        return newMemberId;
    }

    public void setNewMemberId(String newMemberId) {
        this.newMemberId = newMemberId;
    }

    public String getQxmCreatorId() {
        return qxmCreatorId;
    }

    public void setQxmCreatorId(String qxmCreatorId) {
        this.qxmCreatorId = qxmCreatorId;
    }

    public String getQxmCreatorName() {
        return qxmCreatorName;
    }

    public void setQxmCreatorName(String qxmCreatorName) {
        this.qxmCreatorName = qxmCreatorName;
    }

    public String getUserImage() {
        if (!TextUtils.isEmpty(userImage)) {
            // as image might be from facebook or google as through social login
            if (userImage.contains("facebook") || userImage.contains("googleusercontent"))
                return userImage;
            return IMAGE_SERVER_ROOT + userImage;
        }
        return "";
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getProfileImage() {
        if (!TextUtils.isEmpty(profileImage)) {
            // as image might be from facebook or google as through social login
            if (profileImage.contains("facebook") || profileImage.contains("googleusercontent"))
                return profileImage;
            return IMAGE_SERVER_ROOT + profileImage;
        }
        return "";
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public String getGroupImage() {
        if (!TextUtils.isEmpty(groupImage)) {
            // as image might be from facebook or google as through social login
            if (groupImage.contains("facebook") || groupImage.contains("googleusercontent"))
                return groupImage;
            return IMAGE_SERVER_ROOT + groupImage;
        }
        return "";
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    @Override
    public String toString() {
        return "NotificationItem{" +
                "pollName='" + pollName + '\'' +
                ", pollCreator='" + pollCreator + '\'' +
                ", pollId='" + pollId + '\'' +
                ", time='" + time + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", qxmCreator='" + qxmCreator + '\'' +
                ", qxmId='" + qxmId + '\'' +
                ", qxmName='" + qxmName + '\'' +
                ", participatorId='" + participatorId + '\'' +
                ", participatorName='" + participatorName + '\'' +
                ", followerName='" + followerName + '\'' +
                ", followingName='" + followingName + '\'' +
                ", followingId='" + followingId + '\'' +
                ", followerId='" + followerId + '\'' +
                ", enrollUserName='" + enrollUserName + '\'' +
                ", singleMCQName='" + singleMCQName + '\'' +
                ", singleMCQCreator='" + singleMCQCreator + '\'' +
                ", singleMCQId='" + singleMCQId + '\'' +
                ", singleQxmCreator='" + singleQxmCreator + '\'' +
                ", singleQxmName='" + singleQxmName + '\'' +
                ", singleQxmId='" + singleQxmId + '\'' +
                ", adminName='" + adminName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", newMemberName='" + newMemberName + '\'' +
                ", newMemberId='" + newMemberId + '\'' +
                ", qxmCreatorId='" + qxmCreatorId + '\'' +
                ", qxmCreatorName='" + qxmCreatorName + '\'' +
                ", userImage='" + userImage + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", groupImage='" + groupImage + '\'' +
                '}';
    }
}
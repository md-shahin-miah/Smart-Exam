package com.crux.qxm.db.models.poll.pollOverview;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class SinglePollData {

    @SerializedName("lastSubmissionDate")
    private String lastSubmissionDate;

    @SerializedName("feedThumbnailURL")
    private String feedThumbnailURL;

    @SerializedName("pollPerticipator")
    private int pollPerticipator;

    @SerializedName("feedPrivacy")
    private String feedPrivacy;

    @SerializedName("feedSingleMultiOptions")
    private List<String> feedSingleMultiOptions;

    @SerializedName("isParticipated")
    private boolean isParticipated;

    @SerializedName("feedCreatorImageURL")
    private String feedCreatorImageURL;

    @SerializedName("feedOwner")
    private String feedCreatorId;

    @SerializedName("feedParticipantsQuantity")
    private int feedParticipantsQuantity;

    @SerializedName("feedCreatorName")
    private String feedCreatorName;

    @SerializedName("feedEnrollAcceptedCount")
    private int feedEnrollAcceptedCount;

    @SerializedName("isEnrollPending")
    private boolean isEnrollPending;

    @SerializedName("pollOptions")
    private List<List<PollOptionsItemItem>> pollOptions;

    @SerializedName("feedDescription")
    private String feedDescription;

    @SerializedName("feedQxmId")
    private String feedQxmId;

    @SerializedName("feedPollId")
    private String feedPollId;

    @SerializedName("feedViewType")
    private String feedViewType;

    @SerializedName("isMeParticatedOnPoll")
    private Object isMeParticatedOnPoll;

    @SerializedName("feedCategory")
    private List<String> feedCategory;

    @SerializedName("correctAnswerVisibilityDate")
    private String correctAnswerVisibilityDate;

    @SerializedName("editedFlag")
    private boolean editedFlag;

    @SerializedName("totalPollCount")
    private int totalPollCount;

    @SerializedName("isContestModeOn")
    private boolean isContestModeOn;

    @SerializedName("isClickedIgnited")
    private boolean isClickedIgnited;

    @SerializedName("questionVisibility")
    private String questionVisibility;

    @SerializedName("feedYoutubeLinkURL")
    private String feedYoutubeLinkURL;

    @SerializedName("qxmExpirationDate")
    private String qxmExpirationDate;

    @SerializedName("enrollStatus")
    private boolean enrollStatus;

    @SerializedName("isEnrollAccepted")
    private boolean isEnrollAccepted;

    @SerializedName("feedType")
    private String feedType;

    @SerializedName("feedCreationTime")
    private String feedCreationTime;

    @SerializedName("feedIgniteQuantity")
    private int feedIgniteQuantity;

    @SerializedName("_id")
    private String id;

    @SerializedName("feedTitle")
    private String feedTitle;

    @SerializedName("feedEnrollPendingCount")
    private int feedEnrollPendingCount;

    @SerializedName("qxmCurrentStatus")
    private String qxmCurrentStatus;

    @SerializedName("feedShareQuantity")
    private int feedShareQuantity;

    public String getLastSubmissionDate() {
        return lastSubmissionDate;
    }

    public void setLastSubmissionDate(String lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }

    public String getFeedThumbnailURL() {
        return feedThumbnailURL;
    }

    public void setFeedThumbnailURL(String feedThumbnailURL) {
        this.feedThumbnailURL = feedThumbnailURL;
    }

    public int getPollPerticipator() {
        return pollPerticipator;
    }

    public void setPollPerticipator(int pollPerticipator) {
        this.pollPerticipator = pollPerticipator;
    }

    public String getFeedPrivacy() {
        return feedPrivacy;
    }

    public void setFeedPrivacy(String feedPrivacy) {
        this.feedPrivacy = feedPrivacy;
    }

    public List<String> getFeedSingleMultiOptions() {
        return feedSingleMultiOptions;
    }

    public void setFeedSingleMultiOptions(List<String> feedSingleMultiOptions) {
        this.feedSingleMultiOptions = feedSingleMultiOptions;
    }

    public boolean isParticipated() {
        return isParticipated;
    }

    public void setParticipated(boolean participated) {
        isParticipated = participated;
    }

    public String getFeedCreatorImageURL() {

        if(feedCreatorImageURL!=null){
            // as image might be from facebook or google as through social login
            if (feedCreatorImageURL.contains("facebook") || feedCreatorImageURL.contains("googleusercontent"))
                return feedCreatorImageURL;

            return IMAGE_SERVER_ROOT + feedCreatorImageURL;
        }
        return "";
    }

    public void setFeedCreatorImageURL(String feedCreatorImageURL) {
        this.feedCreatorImageURL = feedCreatorImageURL;
    }

    public String getFeedCreatorId() {
        return feedCreatorId;
    }

    public void setFeedCreatorId(String feedCreatorId) {
        this.feedCreatorId = feedCreatorId;
    }

    public int getFeedParticipantsQuantity() {
        return feedParticipantsQuantity;
    }

    public void setFeedParticipantsQuantity(int feedParticipantsQuantity) {
        this.feedParticipantsQuantity = feedParticipantsQuantity;
    }

    public String getFeedCreatorName() {
        return feedCreatorName;
    }

    public void setFeedCreatorName(String feedCreatorName) {
        this.feedCreatorName = feedCreatorName;
    }

    public int getFeedEnrollAcceptedCount() {
        return feedEnrollAcceptedCount;
    }

    public void setFeedEnrollAcceptedCount(int feedEnrollAcceptedCount) {
        this.feedEnrollAcceptedCount = feedEnrollAcceptedCount;
    }

    public boolean isEnrollPending() {
        return isEnrollPending;
    }

    public void setEnrollPending(boolean enrollPending) {
        isEnrollPending = enrollPending;
    }

    public List<List<PollOptionsItemItem>> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(List<List<PollOptionsItemItem>> pollOptions) {
        this.pollOptions = pollOptions;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getFeedQxmId() {
        return feedQxmId;
    }

    public void setFeedQxmId(String feedQxmId) {
        this.feedQxmId = feedQxmId;
    }

    public String getFeedPollId() {
        return feedPollId;
    }

    public void setFeedPollId(String feedPollId) {
        this.feedPollId = feedPollId;
    }

    public String getFeedViewType() {
        return feedViewType;
    }

    public void setFeedViewType(String feedViewType) {
        this.feedViewType = feedViewType;
    }

    public Object getIsMeParticatedOnPoll() {
        return isMeParticatedOnPoll;
    }

    public void setIsMeParticatedOnPoll(Object isMeParticatedOnPoll) {
        this.isMeParticatedOnPoll = isMeParticatedOnPoll;
    }

    public List<String> getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(List<String> feedCategory) {
        this.feedCategory = feedCategory;
    }

    public String getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(String correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }

    public boolean isEditedFlag() {
        return editedFlag;
    }

    public void setEditedFlag(boolean editedFlag) {
        this.editedFlag = editedFlag;
    }

    public int getTotalPollCount() {
        return totalPollCount;
    }

    public void setTotalPollCount(int totalPollCount) {
        this.totalPollCount = totalPollCount;
    }

    public boolean isContestModeOn() {
        return isContestModeOn;
    }

    public void setContestModeOn(boolean contestModeOn) {
        isContestModeOn = contestModeOn;
    }

    public boolean isClickedIgnited() {
        return isClickedIgnited;
    }

    public void setClickedIgnited(boolean clickedIgnited) {
        isClickedIgnited = clickedIgnited;
    }

    public String getQuestionVisibility() {
        return questionVisibility;
    }

    public void setQuestionVisibility(String questionVisibility) {
        this.questionVisibility = questionVisibility;
    }

    public String getFeedYoutubeLinkURL() {
        return feedYoutubeLinkURL;
    }

    public void setFeedYoutubeLinkURL(String feedYoutubeLinkURL) {
        this.feedYoutubeLinkURL = feedYoutubeLinkURL;
    }

    public String getQxmExpirationDate() {
        return qxmExpirationDate;
    }

    public void setQxmExpirationDate(String qxmExpirationDate) {
        this.qxmExpirationDate = qxmExpirationDate;
    }

    public boolean isEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(boolean enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public boolean isEnrollAccepted() {
        return isEnrollAccepted;
    }

    public void setEnrollAccepted(boolean enrollAccepted) {
        isEnrollAccepted = enrollAccepted;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getFeedCreationTime() {
        return feedCreationTime;
    }

    public void setFeedCreationTime(String feedCreationTime) {
        this.feedCreationTime = feedCreationTime;
    }

    public int getFeedIgniteQuantity() {
        return feedIgniteQuantity;
    }

    public void setFeedIgniteQuantity(int feedIgniteQuantity) {
        this.feedIgniteQuantity = feedIgniteQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public int getFeedEnrollPendingCount() {
        return feedEnrollPendingCount;
    }

    public void setFeedEnrollPendingCount(int feedEnrollPendingCount) {
        this.feedEnrollPendingCount = feedEnrollPendingCount;
    }

    public String getQxmCurrentStatus() {
        return qxmCurrentStatus;
    }

    public void setQxmCurrentStatus(String qxmCurrentStatus) {
        this.qxmCurrentStatus = qxmCurrentStatus;
    }

    public int getFeedShareQuantity() {
        return feedShareQuantity;
    }

    public void setFeedShareQuantity(int feedShareQuantity) {
        this.feedShareQuantity = feedShareQuantity;
    }

    @Override
    public String toString() {
        return "SinglePollData{" +
                "lastSubmissionDate='" + lastSubmissionDate + '\'' +
                ", feedThumbnailURL='" + feedThumbnailURL + '\'' +
                ", pollPerticipator=" + pollPerticipator +
                ", feedPrivacy='" + feedPrivacy + '\'' +
                ", feedSingleMultiOptions=" + feedSingleMultiOptions +
                ", isParticipated=" + isParticipated +
                ", feedCreatorImageURL='" + feedCreatorImageURL + '\'' +
                ", feedCreatorId='" + feedCreatorId + '\'' +
                ", feedParticipantsQuantity=" + feedParticipantsQuantity +
                ", feedCreatorName='" + feedCreatorName + '\'' +
                ", feedEnrollAcceptedCount=" + feedEnrollAcceptedCount +
                ", isEnrollPending=" + isEnrollPending +
                ", pollOptions=" + pollOptions +
                ", feedDescription='" + feedDescription + '\'' +
                ", feedQxmId='" + feedQxmId + '\'' +
                ", feedPollId='" + feedPollId + '\'' +
                ", feedViewType='" + feedViewType + '\'' +
                ", isMeParticatedOnPoll=" + isMeParticatedOnPoll +
                ", feedCategory=" + feedCategory +
                ", correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", editedFlag=" + editedFlag +
                ", totalPollCount=" + totalPollCount +
                ", isContestModeOn=" + isContestModeOn +
                ", isClickedIgnited=" + isClickedIgnited +
                ", questionVisibility='" + questionVisibility + '\'' +
                ", feedYoutubeLinkURL='" + feedYoutubeLinkURL + '\'' +
                ", qxmExpirationDate='" + qxmExpirationDate + '\'' +
                ", enrollStatus=" + enrollStatus +
                ", isEnrollAccepted=" + isEnrollAccepted +
                ", feedType='" + feedType + '\'' +
                ", feedCreationTime='" + feedCreationTime + '\'' +
                ", feedIgniteQuantity=" + feedIgniteQuantity +
                ", id='" + id + '\'' +
                ", feedTitle='" + feedTitle + '\'' +
                ", feedEnrollPendingCount=" + feedEnrollPendingCount +
                ", qxmCurrentStatus='" + qxmCurrentStatus + '\'' +
                ", feedShareQuantity=" + feedShareQuantity +
                '}';
    }
}
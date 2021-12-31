package com.crux.qxm.db.models.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.feedDataWithShared.FeedInGroupItem;
import com.crux.qxm.db.models.poll.PollOption;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;
import static com.crux.qxm.utils.StaticValues.YOUTUBE_IMAGE_ROOT;

// Todo feature-feed merged with feature-question-overview

public class FeedData implements Parcelable {
    @SerializedName("_id")
    private String feedId;
    @SerializedName("feedQxmId")
    private String feedQxmId;
    @SerializedName("feedTitle")
    private String feedName;
    @SerializedName("feedPrivacy")
    private String feedPrivacy;
    @SerializedName("feedCreationTime")
    private String feedCreationTime;
    @SerializedName("feedTotalTime")
    private String feedExamDuration;
    @SerializedName("feedOwner")
    private String feedCreatorId;
    @SerializedName("feedCreatorName")
    private String feedCreatorName;
    @SerializedName("feedCreatorImageURL")
    private String feedCreatorImageURL;
    @SerializedName("feedThumbnailURL")
    private String feedThumbnailURL;
    @SerializedName("enrollStatus")
    private boolean isEnrollEnabled;
    @SerializedName("isEnrollPending")
    private boolean isEnrollPending;
    @SerializedName("isEnrollAccepted")
    private boolean isEnrollAccepted;
    @SerializedName("feedYoutubeLinkURL")
    private String feedYoutubeLinkURL;
    @SerializedName("feedDescription")
    private String feedDescription;
    @SerializedName("feedParticipantsQuantity")
    private String feedParticipantsQuantity;
    @SerializedName("pollPerticipator")
    private String pollParticipatorQuantity;
    @SerializedName("feedIgniteQuantity")
    private String feedIgniteQuantity;
    @SerializedName("feedShareQuantity")
    private String feedShareQuantity;
    @SerializedName("isSharedByMe")
    private boolean isSharedByMe;
    @SerializedName("isContestModeOn")
    private boolean isContestModeOn;
    @SerializedName("isClickedIgnited")
    private boolean isIgnitedClicked;
    @SerializedName("isParticipated")
    private boolean isParticipated;
    @SerializedName("lastSubmissionDate")
    private String lastSubmissionDate;
    @SerializedName("qxmCurrentStatus")
    private String qxmCurrentStatus;
    @SerializedName("correctAnswerVisibilityDate")
    private String correctAnswerVisibilityDate;

    @SerializedName("qxmExpirationDate")
    private String qxmExpirationDate;

    @SerializedName("questionVisibility")
    private String questionVisibility;

    @SerializedName("feedPendingEvaluationQuantity")
    private String feedPendingEvaluationQuantity;

    @SerializedName("feedEnrollAcceptedCount")
    private String feedEnrollAcceptedQuantity;

    @SerializedName("feedCategory")
    private ArrayList<String> feedCategory;

    @SerializedName("resultStatus")
    private String resultStatus;

    @SerializedName("evaluationType")
    private String evaluationType;

    // region new Added fields for Poll

    @SerializedName("pollOptions")
    private List<PollOption> pollOptions;
    @SerializedName("feedPollId")
    private String feedPollId;
    @SerializedName("feedType")
    private String feedType;
    @SerializedName("feedViewType")
    private String feedViewType;
    @SerializedName("feedSingleMultiOptions")
    private ArrayList<String> singleMCQoptions;
    @SerializedName("negativePoints")
    private String negativePoints;
    @SerializedName("feedInGroup")
    private List<FeedInGroupItem> feedInGroup;
    @SerializedName("qxmStartScheduleDate")
    private String qxmStartScheduleDate;
    @SerializedName("editedFlag")
    private boolean editedFlag;
    @SerializedName("feedEnrollPendingCount")
    private String feedEnrollPendingCount;
    @SerializedName("leaderboardPublishDate")
    private String leaderboardPublishDate;
    @SerializedName("leaderboardPrivacy")
    private String leaderboardPrivacy;
    @SerializedName("isParticipateAfterContestEnd")
    private boolean isParticipateAfterContestEnd;


    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

    public String getFeedPollId() {
        return feedPollId;
    }

    public void setFeedPollId(String feedPollId) {
        this.feedPollId = feedPollId;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getFeedViewType() {
        return feedViewType;
    }

    public void setFeedViewType(String feedViewType) {
        this.feedViewType = feedViewType;
    }

    public String getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(String negativePoints) {
        this.negativePoints = negativePoints;
    }

    // endregion


    // empty constructor
    public FeedData() {
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedQxmId() {
        return feedQxmId;
    }

    public void setFeedQxmId(String feedQxmId) {
        this.feedQxmId = feedQxmId;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedPrivacy() {
        return feedPrivacy;
    }

    public void setFeedPrivacy(String feedPrivacy) {
        this.feedPrivacy = feedPrivacy;
    }

    public String getFeedCreationTime() {
        return feedCreationTime;
    }

    public void setFeedCreationTime(String feedCreationTime) {
        this.feedCreationTime = feedCreationTime;
    }

    public String getFeedExamDuration() {
        return feedExamDuration;
    }

    public void setFeedExamDuration(String feedExamDuration) {
        this.feedExamDuration = feedExamDuration;
    }

    public String getFeedCreatorId() {
        return feedCreatorId;
    }

    public void setFeedCreatorId(String feedCreatorId) {
        this.feedCreatorId = feedCreatorId;
    }

    public String getFeedCreatorName() {
        return feedCreatorName;
    }

    public void setFeedCreatorName(String feedCreatorName) {
        this.feedCreatorName = feedCreatorName;
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

    public String getFeedThumbnailURL() {

        if (feedThumbnailURL != null && !feedThumbnailURL.isEmpty() && !feedThumbnailURL.contains(YOUTUBE_IMAGE_ROOT))
            return IMAGE_SERVER_ROOT + feedThumbnailURL;
        return feedThumbnailURL;
    }

    public void setFeedThumbnailURL(String feedThumbnailURL) {
        this.feedThumbnailURL = feedThumbnailURL;
    }

    public boolean isEnrollEnabled() {
        return isEnrollEnabled;
    }

    public void setEnrollEnabled(boolean enrollEnabled) {
        isEnrollEnabled = enrollEnabled;
    }

    public boolean isEnrollPending() {
        return isEnrollPending;
    }

    public void setEnrollPending(boolean enrollPending) {
        isEnrollPending = enrollPending;
    }

    public boolean isEnrollAccepted() {
        return isEnrollAccepted;
    }

    public void setEnrollAccepted(boolean enrollAccepted) {
        isEnrollAccepted = enrollAccepted;
    }

    public String getFeedYoutubeLinkURL() {
        return feedYoutubeLinkURL;
    }

    public void setFeedYoutubeLinkURL(String feedYoutubeLinkURL) {
        this.feedYoutubeLinkURL = feedYoutubeLinkURL;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getPollParticipatorQuantity() {
        return pollParticipatorQuantity;
    }

    public void setPollParticipatorQuantity(String pollParticipatorQuantity) {
        this.pollParticipatorQuantity = pollParticipatorQuantity;
    }

    public String getFeedParticipantsQuantity() {
        return feedParticipantsQuantity;
    }

    public void setFeedParticipantsQuantity(String feedParticipantsQuantity) {
        this.feedParticipantsQuantity = feedParticipantsQuantity;
    }

    public String getFeedIgniteQuantity() {
        return feedIgniteQuantity;
    }

    public void setFeedIgniteQuantity(String feedIgniteQuantity) {
        this.feedIgniteQuantity = feedIgniteQuantity;
    }

    public String getFeedShareQuantity() {
        return feedShareQuantity;
    }

    public void setFeedShareQuantity(String feedShareQuantity) {
        this.feedShareQuantity = feedShareQuantity;
    }

    public boolean isSharedByMe() {
        return isSharedByMe;
    }

    public void setSharedByMe(boolean sharedByMe) {
        isSharedByMe = sharedByMe;
    }

    public boolean isContestModeOn() {
        return isContestModeOn;
    }

    public void setContestModeOn(boolean contestModeOn) {
        isContestModeOn = contestModeOn;
    }

    public boolean isIgnitedClicked() {
        return isIgnitedClicked;
    }

    public void setIgnitedClicked(boolean ignitedClicked) {
        isIgnitedClicked = ignitedClicked;
    }

    public boolean isParticipated() {
        return isParticipated;
    }

    public void setParticipated(boolean participated) {
        isParticipated = participated;
    }

    public String getLastSubmissionDate() {
        return lastSubmissionDate;
    }

    public void setLastSubmissionDate(String lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }

    public String getCorrectAnswerVisibilityDate() {
        return correctAnswerVisibilityDate;
    }

    public void setCorrectAnswerVisibilityDate(String correctAnswerVisibilityDate) {
        this.correctAnswerVisibilityDate = correctAnswerVisibilityDate;
    }


    public String getQxmCurrentStatus() {
        return qxmCurrentStatus;
    }

    public void setQxmCurrentStatus(String qxmCurrentStatus) {
        this.qxmCurrentStatus = qxmCurrentStatus;
    }

    public String getQxmExpirationDate() {
        return qxmExpirationDate;
    }

    public void setQxmExpirationDate(String qxmExpirationDate) {
        this.qxmExpirationDate = qxmExpirationDate;
    }

    public String getQuestionVisibility() {
        return questionVisibility;
    }

    public void setQuestionVisibility(String questionVisibility) {
        this.questionVisibility = questionVisibility;
    }

    public String getFeedPendingEvaluationQuantity() {
        return feedPendingEvaluationQuantity;
    }

    public void setFeedPendingEvaluationQuantity(String feedPendingEvaluationQuantity) {
        this.feedPendingEvaluationQuantity = feedPendingEvaluationQuantity;
    }

    public String getFeedEnrollAcceptedQuantity() {
        return feedEnrollAcceptedQuantity;
    }

    public void setFeedEnrollAcceptedQuantity(String feedEnrollAcceptedQuantity) {
        this.feedEnrollAcceptedQuantity = feedEnrollAcceptedQuantity;
    }

    public ArrayList<String> getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(ArrayList<String> feedCategory) {
        this.feedCategory = feedCategory;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<FeedInGroupItem> getFeedInGroup() {
        return feedInGroup;
    }

    public void setFeedInGroup(List<FeedInGroupItem> feedInGroup) {
        this.feedInGroup = feedInGroup;
    }

    public ArrayList<String> getSingleMCQoptions() {
        return singleMCQoptions;
    }

    public void setSingleMCQoptions(ArrayList<String> singleMCQoptions) {
        this.singleMCQoptions = singleMCQoptions;
    }

    public String getQxmStartScheduleDate() {
        return qxmStartScheduleDate;
    }

    public void setQxmStartScheduleDate(String qxmStartScheduleDate) {
        this.qxmStartScheduleDate = qxmStartScheduleDate;
    }

    public boolean isEditedFlag() {
        return editedFlag;
    }

    public void setEditedFlag(boolean editedFlag) {
        this.editedFlag = editedFlag;
    }

    public String getFeedEnrollPendingCount() {
        return feedEnrollPendingCount;
    }

    public void setFeedEnrollPendingCount(String feedEnrollPendingCount) {
        this.feedEnrollPendingCount = feedEnrollPendingCount;
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

    public boolean isParticipateAfterContestEnd() {
        return isParticipateAfterContestEnd;
    }

    public void setParticipateAfterContestEnd(boolean participateAfterContestEnd) {
        isParticipateAfterContestEnd = participateAfterContestEnd;
    }

    protected FeedData(Parcel in) {
        this.feedId = in.readString();
        this.feedQxmId = in.readString();
        this.feedName = in.readString();
        this.feedPrivacy = in.readString();
        this.feedCreationTime = in.readString();
        this.feedExamDuration = in.readString();
        this.feedCreatorId = in.readString();
        this.feedCreatorName = in.readString();
        this.feedCreatorImageURL = in.readString();
        this.feedThumbnailURL = in.readString();
        this.isEnrollEnabled = in.readByte() != 0;
        this.isEnrollPending = in.readByte() != 0;
        this.isEnrollAccepted = in.readByte() != 0;
        this.feedYoutubeLinkURL = in.readString();
        this.feedDescription = in.readString();
        this.feedParticipantsQuantity = in.readString();
        this.pollParticipatorQuantity = in.readString();
        this.feedIgniteQuantity = in.readString();
        this.feedShareQuantity = in.readString();
        this.isSharedByMe = in.readByte() != 0;
        this.isContestModeOn = in.readByte() != 0;
        this.isIgnitedClicked = in.readByte() != 0;
        this.isParticipated = in.readByte() != 0;
        this.lastSubmissionDate = in.readString();
        this.qxmCurrentStatus = in.readString();
        this.correctAnswerVisibilityDate = in.readString();
        this.qxmExpirationDate = in.readString();
        this.questionVisibility = in.readString();
        this.feedPendingEvaluationQuantity = in.readString();
        this.feedEnrollAcceptedQuantity = in.readString();
        this.feedCategory = in.createStringArrayList();
        this.resultStatus = in.readString();
        this.evaluationType = in.readString();
        this.pollOptions = in.createTypedArrayList(PollOption.CREATOR);
        this.feedPollId = in.readString();
        this.feedType = in.readString();
        this.feedViewType = in.readString();
        this.singleMCQoptions = in.createStringArrayList();
        this.negativePoints = in.readString();
        this.feedInGroup = new ArrayList<FeedInGroupItem>();
        in.readList(this.feedInGroup, FeedInGroupItem.class.getClassLoader());
        this.qxmStartScheduleDate = in.readString();
        this.editedFlag = in.readByte() != 0;
        this.feedEnrollPendingCount = in.readString();
        this.leaderboardPublishDate = in.readString();
        this.leaderboardPrivacy = in.readString();
        this.isParticipateAfterContestEnd = in.readByte() != 0;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "FeedData{" +
                "feedId='" + feedId + '\'' +
                ", feedQxmId='" + feedQxmId + '\'' +
                ", feedName='" + feedName + '\'' +
                ", feedPrivacy='" + feedPrivacy + '\'' +
                ", feedCreationTime='" + feedCreationTime + '\'' +
                ", feedExamDuration='" + feedExamDuration + '\'' +
                ", feedCreatorId='" + feedCreatorId + '\'' +
                ", feedCreatorName='" + feedCreatorName + '\'' +
                ", feedCreatorImageURL='" + feedCreatorImageURL + '\'' +
                ", feedThumbnailURL='" + feedThumbnailURL + '\'' +
                ", isEnrollEnabled=" + isEnrollEnabled +
                ", isEnrollPending=" + isEnrollPending +
                ", isEnrollAccepted=" + isEnrollAccepted +
                ", feedYoutubeLinkURL='" + feedYoutubeLinkURL + '\'' +
                ", feedDescription='" + feedDescription + '\'' +
                ", feedParticipantsQuantity='" + feedParticipantsQuantity + '\'' +
                ", pollParticipatorQuantity='" + pollParticipatorQuantity + '\'' +
                ", feedIgniteQuantity='" + feedIgniteQuantity + '\'' +
                ", feedShareQuantity='" + feedShareQuantity + '\'' +
                ", isSharedByMe=" + isSharedByMe +
                ", isContestModeOn=" + isContestModeOn +
                ", isIgnitedClicked=" + isIgnitedClicked +
                ", isParticipated=" + isParticipated +
                ", lastSubmissionDate='" + lastSubmissionDate + '\'' +
                ", qxmCurrentStatus='" + qxmCurrentStatus + '\'' +
                ", correctAnswerVisibilityDate='" + correctAnswerVisibilityDate + '\'' +
                ", qxmExpirationDate='" + qxmExpirationDate + '\'' +
                ", questionVisibility='" + questionVisibility + '\'' +
                ", feedPendingEvaluationQuantity='" + feedPendingEvaluationQuantity + '\'' +
                ", feedEnrollAcceptedQuantity='" + feedEnrollAcceptedQuantity + '\'' +
                ", feedCategory=" + feedCategory +
                ", resultStatus='" + resultStatus + '\'' +
                ", evaluationType='" + evaluationType + '\'' +
                ", pollOptions=" + pollOptions +
                ", feedPollId='" + feedPollId + '\'' +
                ", feedType='" + feedType + '\'' +
                ", feedViewType='" + feedViewType + '\'' +
                ", singleMCQoptions=" + singleMCQoptions +
                ", negativePoints='" + negativePoints + '\'' +
                ", feedInGroup=" + feedInGroup +
                ", qxmStartScheduleDate='" + qxmStartScheduleDate + '\'' +
                ", editedFlag=" + editedFlag +
                ", feedEnrollPendingCount='" + feedEnrollPendingCount + '\'' +
                ", leaderboardPublishDate='" + leaderboardPublishDate + '\'' +
                ", leaderboardPrivacy='" + leaderboardPrivacy + '\'' +
                ", isParticipateAfterContestEnd=" + isParticipateAfterContestEnd +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.feedId);
        dest.writeString(this.feedQxmId);
        dest.writeString(this.feedName);
        dest.writeString(this.feedPrivacy);
        dest.writeString(this.feedCreationTime);
        dest.writeString(this.feedExamDuration);
        dest.writeString(this.feedCreatorId);
        dest.writeString(this.feedCreatorName);
        dest.writeString(this.feedCreatorImageURL);
        dest.writeString(this.feedThumbnailURL);
        dest.writeByte(this.isEnrollEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEnrollPending ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEnrollAccepted ? (byte) 1 : (byte) 0);
        dest.writeString(this.feedYoutubeLinkURL);
        dest.writeString(this.feedDescription);
        dest.writeString(this.feedParticipantsQuantity);
        dest.writeString(this.pollParticipatorQuantity);
        dest.writeString(this.feedIgniteQuantity);
        dest.writeString(this.feedShareQuantity);
        dest.writeByte(this.isSharedByMe ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isContestModeOn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isIgnitedClicked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isParticipated ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastSubmissionDate);
        dest.writeString(this.qxmCurrentStatus);
        dest.writeString(this.correctAnswerVisibilityDate);
        dest.writeString(this.qxmExpirationDate);
        dest.writeString(this.questionVisibility);
        dest.writeString(this.feedPendingEvaluationQuantity);
        dest.writeString(this.feedEnrollAcceptedQuantity);
        dest.writeStringList(this.feedCategory);
        dest.writeString(this.resultStatus);
        dest.writeString(this.evaluationType);
        dest.writeTypedList(this.pollOptions);
        dest.writeString(this.feedPollId);
        dest.writeString(this.feedType);
        dest.writeString(this.feedViewType);
        dest.writeStringList(this.singleMCQoptions);
        dest.writeString(this.negativePoints);
        dest.writeList(this.feedInGroup);
        dest.writeString(this.qxmStartScheduleDate);
        dest.writeByte(this.editedFlag ? (byte) 1 : (byte) 0);
        dest.writeString(this.feedEnrollPendingCount);
        dest.writeString(this.leaderboardPublishDate);
        dest.writeString(this.leaderboardPrivacy);
        dest.writeByte(this.isParticipateAfterContestEnd ? (byte) 1 : (byte) 0);
    }

    public static final Creator<FeedData> CREATOR = new Creator<FeedData>() {
        @Override
        public FeedData createFromParcel(Parcel source) {
            return new FeedData(source);
        }

        @Override
        public FeedData[] newArray(int size) {
            return new FeedData[size];
        }
    };
}
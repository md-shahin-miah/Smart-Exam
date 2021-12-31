package com.crux.qxm.db.models.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.poll.PollOption;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class FeedDataPoll implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("pollOptions")
    private List<PollOption> pollOptions;
    @SerializedName("feedCreatorName")
    private String feedCreatorName;
    @SerializedName("feedCreatorImageURL")
    private String feedCreatorImageURL;
    @SerializedName("feedPrivacy")
    private String feedPrivacy;
    @SerializedName("feedCreationTime")
    private String feedCreationTime;
    @SerializedName("feedOwner")
    private String feedCreatorId;
    @SerializedName("feedTitle")
    private String feedName;
    @SerializedName("feedThumbnailURL")
    private String feedThumbnailURL;
    @SerializedName("feedYoutubeLinkURL")
    private String feedYoutubeLinkURL;
    @SerializedName("feedDescription")
    private String feedDescription;
    @SerializedName("feedParticipantsQuantity")
    private String feedParticipantsQuantity;
    @SerializedName("feedIgniteQuantity")
    private String feedIgniteQuantity;
    @SerializedName("feedShareQuantity")
    private String feedShareQuantity;
    @SerializedName("isSharedByMe")
    private boolean isSharedByMe;
    @SerializedName("isClickedIgnited")
    private boolean isIgnitedClicked;
    @SerializedName("isParticipated")
    private boolean isParticipated;
    @SerializedName("feedPollId")
    private String feedPollId;
    @SerializedName("feedType")
    private String feedType;
    @SerializedName("feedViewType")
    private String feedViewType;

    public FeedDataPoll() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
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

    public String getFeedCreatorId() {
        return feedCreatorId;
    }

    public void setFeedCreatorId(String feedCreatorId) {
        this.feedCreatorId = feedCreatorId;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedThumbnailURL() {
        return feedThumbnailURL;
    }

    public void setFeedThumbnailURL(String feedThumbnailURL) {
        this.feedThumbnailURL = feedThumbnailURL;
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

    @Override
    public String toString() {
        return "FeedDataPoll{" +
                "id='" + id + '\'' +
                ", pollOptions=" + pollOptions +
                ", feedCreatorName='" + feedCreatorName + '\'' +
                ", feedCreatorImageURL='" + feedCreatorImageURL + '\'' +
                ", feedPrivacy='" + feedPrivacy + '\'' +
                ", feedCreationTime='" + feedCreationTime + '\'' +
                ", feedCreatorId='" + feedCreatorId + '\'' +
                ", feedName='" + feedName + '\'' +
                ", feedThumbnailURL='" + feedThumbnailURL + '\'' +
                ", feedYoutubeLinkURL='" + feedYoutubeLinkURL + '\'' +
                ", feedDescription='" + feedDescription + '\'' +
                ", feedParticipantsQuantity='" + feedParticipantsQuantity + '\'' +
                ", feedIgniteQuantity='" + feedIgniteQuantity + '\'' +
                ", feedShareQuantity='" + feedShareQuantity + '\'' +
                ", isSharedByMe=" + isSharedByMe +
                ", isIgnitedClicked=" + isIgnitedClicked +
                ", isParticipated=" + isParticipated +
                ", feedPollId='" + feedPollId + '\'' +
                ", feedType='" + feedType + '\'' +
                ", feedViewType='" + feedViewType + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(this.pollOptions);
        dest.writeString(this.feedCreatorName);
        dest.writeString(this.feedCreatorImageURL);
        dest.writeString(this.feedPrivacy);
        dest.writeString(this.feedCreationTime);
        dest.writeString(this.feedCreatorId);
        dest.writeString(this.feedName);
        dest.writeString(this.feedThumbnailURL);
        dest.writeString(this.feedYoutubeLinkURL);
        dest.writeString(this.feedDescription);
        dest.writeString(this.feedParticipantsQuantity);
        dest.writeString(this.feedIgniteQuantity);
        dest.writeString(this.feedShareQuantity);
        dest.writeByte(this.isSharedByMe ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isIgnitedClicked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isParticipated ? (byte) 1 : (byte) 0);
        dest.writeString(this.feedPollId);
        dest.writeString(this.feedType);
        dest.writeString(this.feedViewType);
    }

    protected FeedDataPoll(Parcel in) {
        this.id = in.readString();
        this.pollOptions = in.createTypedArrayList(PollOption.CREATOR);
        this.feedCreatorName = in.readString();
        this.feedCreatorImageURL = in.readString();
        this.feedPrivacy = in.readString();
        this.feedCreationTime = in.readString();
        this.feedCreatorId = in.readString();
        this.feedName = in.readString();
        this.feedThumbnailURL = in.readString();
        this.feedYoutubeLinkURL = in.readString();
        this.feedDescription = in.readString();
        this.feedParticipantsQuantity = in.readString();
        this.feedIgniteQuantity = in.readString();
        this.feedShareQuantity = in.readString();
        this.isSharedByMe = in.readByte() != 0;
        this.isIgnitedClicked = in.readByte() != 0;
        this.isParticipated = in.readByte() != 0;
        this.feedPollId = in.readString();
        this.feedType = in.readString();
        this.feedViewType = in.readString();
    }

    public static final Parcelable.Creator<FeedDataPoll> CREATOR = new Parcelable.Creator<FeedDataPoll>() {
        @Override
        public FeedDataPoll createFromParcel(Parcel source) {
            return new FeedDataPoll(source);
        }

        @Override
        public FeedDataPoll[] newArray(int size) {
            return new FeedDataPoll[size];
        }
    };
}

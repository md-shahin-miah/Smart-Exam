package com.crux.qxm.db.models.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FeedDataCauseOfNotInterested implements Parcelable {
    @SerializedName("_id")
    private String feedId;
    @SerializedName("feedOwner")
    private String feedCreatorId;
    @SerializedName("feedQxmId")
    private String feedQxmId;
    @SerializedName("notInterestedCause")
    private String notInterestedCause;

    private FeedData feedData;

    public FeedDataCauseOfNotInterested() {
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedCreatorId() {
        return feedCreatorId;
    }

    public void setFeedCreatorId(String feedCreatorId) {
        this.feedCreatorId = feedCreatorId;
    }

    public String getFeedQxmId() {
        return feedQxmId;
    }

    public void setFeedQxmId(String feedQxmId) {
        this.feedQxmId = feedQxmId;
    }

    public String getNotInterestedCause() {
        return notInterestedCause;
    }

    public void setNotInterestedCause(String notInterestedCause) {
        this.notInterestedCause = notInterestedCause;
    }

    public FeedData getFeedData() {
        return feedData;
    }

    public void setFeedData(FeedData feedData) {
        this.feedData = feedData;
    }

    @Override
    public String toString() {
        return "FeedDataCauseOfNotInterested{" +
                "feedId='" + feedId + '\'' +
                ", feedCreatorId='" + feedCreatorId + '\'' +
                ", feedQxmId='" + feedQxmId + '\'' +
                ", notInterestedCause='" + notInterestedCause + '\'' +
                ", feedData=" + feedData +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.feedId);
        dest.writeString(this.feedCreatorId);
        dest.writeString(this.feedQxmId);
        dest.writeString(this.notInterestedCause);
        dest.writeParcelable(this.feedData, flags);
    }

    protected FeedDataCauseOfNotInterested(Parcel in) {
        this.feedId = in.readString();
        this.feedCreatorId = in.readString();
        this.feedQxmId = in.readString();
        this.notInterestedCause = in.readString();
        this.feedData = in.readParcelable(FeedData.class.getClassLoader());
    }

    public static final Creator<FeedDataCauseOfNotInterested> CREATOR = new Creator<FeedDataCauseOfNotInterested>() {
        @Override
        public FeedDataCauseOfNotInterested createFromParcel(Parcel source) {
            return new FeedDataCauseOfNotInterested(source);
        }

        @Override
        public FeedDataCauseOfNotInterested[] newArray(int size) {
            return new FeedDataCauseOfNotInterested[size];
        }
    };
}

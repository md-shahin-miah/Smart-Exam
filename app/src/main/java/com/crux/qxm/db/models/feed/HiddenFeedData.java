package com.crux.qxm.db.models.feed;

/**
 * Created by frshafi on 3/21/18.
 */

public class HiddenFeedData {
    private String feedItemRemovedMessage;
    private FeedData feedData;

    public String getFeedItemRemovedMessage() {
        return feedItemRemovedMessage;
    }

    public void setFeedItemRemovedMessage(String feedItemRemovedMessage) {
        this.feedItemRemovedMessage = feedItemRemovedMessage;
    }

    public FeedData getFeedData() {
        return feedData;
    }

    public void setFeedData(FeedData feedData) {
        this.feedData = feedData;
    }

    @Override
    public String toString() {
        return "HiddenFeedData{" +
                "feedItemRemovedMessage='" + feedItemRemovedMessage + '\'' +
                ", feedData=" + feedData +
                '}';
    }
}

package com.crux.qxm.db.models.feed;

import java.util.List;

/**
 * Created by frshafi on 3/21/18.
 */

public class FeedDataInterested {

    private List<FeedData> feedDataListInterested;

    public FeedDataInterested() {
    }

    public FeedDataInterested(List<FeedData> feedDataListInterested) {
        this.feedDataListInterested = feedDataListInterested;
    }

    public List<FeedData> getFeedDataListInterested() {
        return feedDataListInterested;
    }

    public void setFeedDataListInterested(List<FeedData> feedDataListInterested) {
        this.feedDataListInterested = feedDataListInterested;
    }

    @Override
    public String toString() {
        return "FeedDataListInterested: "+feedDataListInterested.toString();
    }
}

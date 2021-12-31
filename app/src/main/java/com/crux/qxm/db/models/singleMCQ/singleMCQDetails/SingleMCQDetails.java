package com.crux.qxm.db.models.singleMCQ.singleMCQDetails;

import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.myQxm.SingleQxmSettings;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleMCQDetails{
	@SerializedName("feedData")
	private FeedData feedData;
	@SerializedName("correctAns")
	private List<String> correctAns;

	@SerializedName("singleMCQSettings")
	private SingleMCQSettings singleMCQSettings;

	public void setFeedData(FeedData feedData){
		this.feedData = feedData;
	}

	public FeedData getFeedData(){
		return feedData;
	}

	public void setCorrectAns(List<String> correctAns){
		this.correctAns = correctAns;
	}

	public List<String> getCorrectAns(){
		return correctAns;
	}

    public SingleMCQSettings getSingleMCQSettings() {
        return singleMCQSettings;
    }

    public void setSingleMCQSettings(SingleMCQSettings singleMCQSettings) {
        this.singleMCQSettings = singleMCQSettings;
    }

    @Override
    public String toString() {
        return "SingleMCQDetails{" +
                "feedData=" + feedData +
                ", correctAns=" + correctAns +
                ", singleMCQSettings=" + singleMCQSettings +
                '}';
    }
}
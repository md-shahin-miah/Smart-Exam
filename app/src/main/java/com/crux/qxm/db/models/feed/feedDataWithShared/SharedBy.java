package com.crux.qxm.db.models.feed.feedDataWithShared;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharedBy{

	@SerializedName("feedInGroup")
	private List<FeedInGroupItem> feedInGroup;

	public void setFeedInGroup(List<FeedInGroupItem> feedInGroup){
		this.feedInGroup = feedInGroup;
	}

	public List<FeedInGroupItem> getFeedInGroup(){
		return feedInGroup;
	}

	@Override
 	public String toString(){
		return 
			"SharedBy{" + 
			"feedInGroup = '" + feedInGroup + '\'' + 
			"}";
		}
}
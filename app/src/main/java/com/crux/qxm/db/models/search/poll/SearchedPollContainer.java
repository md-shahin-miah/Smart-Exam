package com.crux.qxm.db.models.search.poll;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchedPollContainer{

	@SerializedName("pollFollower")
	private List<SearchedPoll> followersPoll;

	@SerializedName("otherPoll")
	private List<SearchedPoll> othersPoll;


	public void setOthersPoll(List<SearchedPoll> othersPoll){
		this.othersPoll = othersPoll;
	}

	public List<SearchedPoll> getOthersPoll(){
		return othersPoll;
	}

	public void setFollowersPoll(List<SearchedPoll> followersPoll){
		this.followersPoll = followersPoll;
	}

	public List<SearchedPoll> getFollowersPoll(){
		return followersPoll;
	}

	@Override
 	public String toString(){
		return 
			"SearchedPollContainer{" + 
			"othersPoll = '" + othersPoll + '\'' +
			",followersPoll = '" + followersPoll + '\'' +
			"}";
		}
}
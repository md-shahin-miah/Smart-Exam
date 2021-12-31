package com.crux.qxm.db.models.poll.pollOverview;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollOptionsItemItem{

	@SerializedName("voteNum")
	private int voteNum;

	@SerializedName("isMeParticatedOnPoll")
	private boolean isMeParticatedOnPoll;

	@SerializedName("voter")
	private List<List<String>> voter;

	@SerializedName("title")
	private String title;

	public void setVoteNum(int voteNum){
		this.voteNum = voteNum;
	}

	public int getVoteNum(){
		return voteNum;
	}

	public void setIsMeParticatedOnPoll(boolean isMeParticatedOnPoll){
		this.isMeParticatedOnPoll = isMeParticatedOnPoll;
	}

	public boolean isIsMeParticatedOnPoll(){
		return isMeParticatedOnPoll;
	}

	public void setVoter(List<List<String>> voter){
		this.voter = voter;
	}

	public List<List<String>> getVoter(){
		return voter;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"PollOptionsItemItem{" + 
			"voteNum = '" + voteNum + '\'' + 
			",isMeParticatedOnPoll = '" + isMeParticatedOnPoll + '\'' + 
			",voter = '" + voter + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}
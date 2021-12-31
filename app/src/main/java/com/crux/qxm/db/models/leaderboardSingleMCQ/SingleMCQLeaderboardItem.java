package com.crux.qxm.db.models.leaderboardSingleMCQ;

import com.google.gson.annotations.SerializedName;

public class SingleMCQLeaderboardItem {

	@SerializedName("questionSet")
	private QuestionSet questionSet;

	@SerializedName("userPerformance")
	private UserPerformance userPerformance;

	@SerializedName("participatedAt")
	private String participatedAt;

	@SerializedName("participator")
	private Participator participator;

	public void setQuestionSet(QuestionSet questionSet){
		this.questionSet = questionSet;
	}

	public QuestionSet getQuestionSet(){
		return questionSet;
	}

	public void setUserPerformance(UserPerformance userPerformance){
		this.userPerformance = userPerformance;
	}

	public UserPerformance getUserPerformance(){
		return userPerformance;
	}

	public void setParticipatedAt(String participatedAt){
		this.participatedAt = participatedAt;
	}

	public String getParticipatedAt(){
		return participatedAt;
	}

	public void setParticipator(Participator participator){
		this.participator = participator;
	}

	public Participator getParticipator(){
		return participator;
	}

	@Override
 	public String toString(){
		return 
			"SingleMCQLeaderboardItem{" +
			"questionSet = '" + questionSet + '\'' + 
			",userPerformance = '" + userPerformance + '\'' + 
			",participatedAt = '" + participatedAt + '\'' + 
			",participator = '" + participator + '\'' + 
			"}";
		}
}
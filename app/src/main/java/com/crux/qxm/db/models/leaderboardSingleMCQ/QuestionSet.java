package com.crux.qxm.db.models.leaderboardSingleMCQ;

import com.google.gson.annotations.SerializedName;

public class QuestionSet{

	@SerializedName("isCorrect")
	private boolean isCorrect;

	public void setIsCorrect(boolean isCorrect){
		this.isCorrect = isCorrect;
	}

	public boolean isIsCorrect(){
		return isCorrect;
	}

	@Override
 	public String toString(){
		return 
			"QuestionSet{" + 
			"isCorrect = '" + isCorrect + '\'' + 
			"}";
		}
}
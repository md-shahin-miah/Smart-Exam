package com.crux.qxm.db.models.leaderboardSingleMCQ;

import com.google.gson.annotations.SerializedName;

public class UserPerformance{

	@SerializedName("achievePoints")
	private String achievePoints;

	public void setAchievePoints(String achievePoints){
		this.achievePoints = achievePoints;
	}

	public String getAchievePoints(){
		return achievePoints;
	}

	@Override
 	public String toString(){
		return 
			"UserPerformance{" + 
			"achievePoints = '" + achievePoints + '\'' + 
			"}";
		}
}
package com.crux.qxm.db.models.leaderboard;

import com.google.gson.annotations.SerializedName;

public class UserPerformance{

	@SerializedName("timeTakenToAnswer")
	private String timeTakenToAnswer;
	@SerializedName("achievePointsInPercentage")
	private String achievePointsInPercentage;
	@SerializedName("achievePoints")
	private String achievePoints;

	public String getTimeTakenToAnswer() {
		return timeTakenToAnswer;
	}

	public void setTimeTakenToAnswer(String timeTakenToAnswer) {
		this.timeTakenToAnswer = timeTakenToAnswer;
	}

	public String getAchievePointsInPercentage() {
		return achievePointsInPercentage;
	}

	public void setAchievePointsInPercentage(String achievePointsInPercentage) {
		this.achievePointsInPercentage = achievePointsInPercentage;
	}

	public String getAchievePoints() {
		return achievePoints;
	}

	public void setAchievePoints(String achievePoints) {
		this.achievePoints = achievePoints;
	}

	@Override
	public String toString() {
		return "UserPerformance{" +
				"timeTakenToAnswer='" + timeTakenToAnswer + '\'' +
				", achievePointsInPercentage='" + achievePointsInPercentage + '\'' +
				", achievePoints='" + achievePoints + '\'' +
				'}';
	}
}
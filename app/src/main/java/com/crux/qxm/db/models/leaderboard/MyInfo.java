package com.crux.qxm.db.models.leaderboard;

import com.google.gson.annotations.SerializedName;

public class MyInfo{

	@SerializedName("userPerformance")
	private UserPerformance userPerformance;
	@SerializedName("participatedAt")
	private String participatedAt;

	public void setUserPerformance(UserPerformance userPerformance){
		this.userPerformance = userPerformance;
	}

	public UserPerformance getUserPerformance(){
		return userPerformance;
	}

	public String getParticipatedAt() {
		return participatedAt;
	}

	public void setParticipatedAt(String participatedAt) {
		this.participatedAt = participatedAt;
	}

	@Override
	public String toString() {
		return "MyInfo{" +
				"userPerformance=" + userPerformance +
				", participatedAt='" + participatedAt + '\'' +
				'}';
	}
}
package com.crux.qxm.db.models.leaderboard;

import com.google.gson.annotations.SerializedName;

public class LeaderBoardItem{

	@SerializedName("userPerformance")
	private UserPerformance userPerformance;

	@SerializedName("participatedAt")
	private String participatedAt;

	@SerializedName("participator")
	private Participator participator;

	public UserPerformance getUserPerformance() {
		return userPerformance;
	}

	public void setUserPerformance(UserPerformance userPerformance) {
		this.userPerformance = userPerformance;
	}

	public String getParticipatedAt() {
		return participatedAt;
	}

	public void setParticipatedAt(String participatedAt) {
		this.participatedAt = participatedAt;
	}

	public Participator getParticipator() {
		return participator;
	}

	public void setParticipator(Participator participator) {
		this.participator = participator;
	}

	@Override
	public String toString() {
		return "LeaderBoardItem{" +
				"userPerformance=" + userPerformance +
				", participatedAt='" + participatedAt + '\'' +
				", participator=" + participator +
				'}';
	}
}
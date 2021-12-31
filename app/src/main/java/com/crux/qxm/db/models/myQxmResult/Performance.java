package com.crux.qxm.db.models.myQxmResult;

import com.google.gson.annotations.SerializedName;

public class Performance{

	@SerializedName("participatedAt")
	private String participatedAt;

	@SerializedName("percentage")
	private String percentage;

	@SerializedName("takenTime")
	private String takenTime;

	@SerializedName("marks")
	private String marks;

	@SerializedName("user")
	private String user;

	public void setParticipatedAt(String participatedAt){
		this.participatedAt = participatedAt;
	}

	public String getParticipatedAt(){
		return participatedAt;
	}

	public void setPercentage(String percentage){
		this.percentage = percentage;
	}

	public String getPercentage(){
		return percentage;
	}

	public void setTakenTime(String takenTime){
		this.takenTime = takenTime;
	}

	public String getTakenTime(){
		return takenTime;
	}

	public void setMarks(String marks){
		this.marks = marks;
	}

	public String getMarks(){
		return marks;
	}

	public void setUser(String user){
		this.user = user;
	}

	public String getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"Performance{" + 
			"participatedAt = '" + participatedAt + '\'' + 
			",percentage = '" + percentage + '\'' + 
			",takenTime = '" + takenTime + '\'' + 
			",marks = '" + marks + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}
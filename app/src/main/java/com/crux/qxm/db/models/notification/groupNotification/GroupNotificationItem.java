package com.crux.qxm.db.models.notification.groupNotification;

import com.google.gson.annotations.SerializedName;

public class GroupNotificationItem{

	@SerializedName("_id")
	private String id;

	@SerializedName("user")
	private User user;

	@SerializedName("status")
	private String status;

	@SerializedName("group")
	private Group group;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setGroup(Group group){
		this.group = group;
	}

	public Group getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"GroupNotificationItem{" + 
			"_id = '" + id + '\'' + 
			",user = '" + user + '\'' + 
			",status = '" + status + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}
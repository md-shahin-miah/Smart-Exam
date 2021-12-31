package com.crux.qxm.db.models.notification.groupNotification;

import com.google.gson.annotations.SerializedName;

public class GroupInfo{

	@SerializedName("groupName")
	private String groupName;

	@SerializedName("groupImage")
	private String groupImage;

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupImage(String groupImage){
		this.groupImage = groupImage;
	}

	public String getGroupImage(){
		return groupImage;
	}

	@Override
 	public String toString(){
		return 
			"GroupInfo{" + 
			"groupName = '" + groupName + '\'' + 
			",groupImage = '" + groupImage + '\'' + 
			"}";
		}
}
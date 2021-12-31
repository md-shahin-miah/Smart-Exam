package com.crux.qxm.db.models.group.groupInvitationsOfUser;

import com.google.gson.annotations.SerializedName;

public class GroupInfo{

	@SerializedName("groupName")
	private String groupName;

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName(){
		return groupName;
	}

	@Override
 	public String toString(){
		return 
			"GroupInfo{" + 
			"groupName = '" + groupName + '\'' + 
			"}";
		}
}
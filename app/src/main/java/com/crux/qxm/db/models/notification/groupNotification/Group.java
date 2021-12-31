package com.crux.qxm.db.models.notification.groupNotification;

import com.google.gson.annotations.SerializedName;

public class Group{

	@SerializedName("_id")
	private String id;

	@SerializedName("groupInfo")
	private GroupInfo groupInfo;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setGroupInfo(GroupInfo groupInfo){
		this.groupInfo = groupInfo;
	}

	public GroupInfo getGroupInfo(){
		return groupInfo;
	}

	@Override
 	public String toString(){
		return 
			"Group{" + 
			"_id = '" + id + '\'' + 
			",groupInfo = '" + groupInfo + '\'' + 
			"}";
		}
}
package com.crux.qxm.db.models.group.groupMemberList;

import com.google.gson.annotations.SerializedName;

public class AdminItem{

	@SerializedName("newAdmin")
	private NewAdmin newAdmin;

	public void setNewAdmin(NewAdmin newAdmin){
		this.newAdmin = newAdmin;
	}

	public NewAdmin getNewAdmin(){
		return newAdmin;
	}

	@Override
 	public String toString(){
		return 
			"AdminItem{" + 
			"newAdmin = '" + newAdmin + '\'' + 
			"}";
		}
}
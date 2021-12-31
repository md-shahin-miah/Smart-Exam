package com.crux.qxm.db.models.group.groupMemberList;

import com.google.gson.annotations.SerializedName;

public class ModeratorItem{

	@SerializedName("newMod")
	private NewMod newMod;

	public void setNewMod(NewMod newMod){
		this.newMod = newMod;
	}

	public NewMod getNewMod(){
		return newMod;
	}

	@Override
 	public String toString(){
		return 
			"ModeratorItem{" + 
			"newMod = '" + newMod + '\'' + 
			"}";
		}
}
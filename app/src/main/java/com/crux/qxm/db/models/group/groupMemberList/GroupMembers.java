package com.crux.qxm.db.models.group.groupMemberList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupMembers{

	@SerializedName("moderator")
	private List<ModeratorItem> moderator;

	@SerializedName("members")
	private List<MembersItem> members;

	@SerializedName("admin")
	private List<AdminItem> admin;

	@SerializedName("_id")
	private String id;

	public void setModerator(List<ModeratorItem> moderator){
		this.moderator = moderator;
	}

	public List<ModeratorItem> getModerator(){
		return moderator;
	}

	public void setMembers(List<MembersItem> members){
		this.members = members;
	}

	public List<MembersItem> getMembers(){
		return members;
	}

	public void setAdmin(List<AdminItem> admin){
		this.admin = admin;
	}

	public List<AdminItem> getAdmin(){
		return admin;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"GroupMembers{" + 
			"moderator = '" + moderator + '\'' + 
			",members = '" + members + '\'' + 
			",admin = '" + admin + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}
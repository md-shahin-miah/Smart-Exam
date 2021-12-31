package com.crux.qxm.db.models.group.groupInvitationsOfUser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupInvitationsOfUser {

	@SerializedName("groupInvitation")
	private List<GroupInvitationItem> groupInvitation;

	@SerializedName("_id")
	private String id;

	public void setGroupInvitation(List<GroupInvitationItem> groupInvitation){
		this.groupInvitation = groupInvitation;
	}

	public List<GroupInvitationItem> getGroupInvitation(){
		return groupInvitation;
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
			"GroupInvitationsOfUser{" +
			"groupInvitation = '" + groupInvitation + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}
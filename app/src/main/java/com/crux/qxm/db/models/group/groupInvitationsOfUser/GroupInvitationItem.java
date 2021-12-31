package com.crux.qxm.db.models.group.groupInvitationsOfUser;

import com.google.gson.annotations.SerializedName;

public class GroupInvitationItem{

	@SerializedName("invitedBy")
	private String invitedBy;

	@SerializedName("_id")
	private String id;

	@SerializedName("status")
	private String status;

	@SerializedName("invitedAt")
	private String invitedAt;

	@SerializedName("group")
	private Group group;

	public void setInvitedBy(String invitedBy){
		this.invitedBy = invitedBy;
	}

	public String getInvitedBy(){
		return invitedBy;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setInvitedAt(String invitedAt){
		this.invitedAt = invitedAt;
	}

	public String getInvitedAt(){
		return invitedAt;
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
			"GroupInvitationItem{" + 
			"invitedBy = '" + invitedBy + '\'' + 
			",_id = '" + id + '\'' + 
			",status = '" + status + '\'' + 
			",invitedAt = '" + invitedAt + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}
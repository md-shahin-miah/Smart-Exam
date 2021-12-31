package com.crux.qxm.db.models.group.groupInvitations;

import com.google.gson.annotations.SerializedName;

public class PendingInviteRequestItem{

	@SerializedName("userInvited")
	private UserInvited userInvited;

	@SerializedName("userInvitedBy")
	private String userInvitedBy;

	@SerializedName("_id")
	private String id;

	@SerializedName("status")
	private String status;

	@SerializedName("invitedAt")
	private String invitedAt;

	public void setUserInvited(UserInvited userInvited){
		this.userInvited = userInvited;
	}

	public UserInvited getUserInvited(){
		return userInvited;
	}

	public void setUserInvitedBy(String userInvitedBy){
		this.userInvitedBy = userInvitedBy;
	}

	public String getUserInvitedBy(){
		return userInvitedBy;
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

	@Override
 	public String toString(){
		return 
			"PendingInviteRequestItem{" + 
			"userInvited = '" + userInvited + '\'' + 
			",userInvitedBy = '" + userInvitedBy + '\'' + 
			",_id = '" + id + '\'' + 
			",status = '" + status + '\'' + 
			",invitedAt = '" + invitedAt + '\'' + 
			"}";
		}
}
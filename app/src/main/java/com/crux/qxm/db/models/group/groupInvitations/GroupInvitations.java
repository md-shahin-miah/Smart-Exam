package com.crux.qxm.db.models.group.groupInvitations;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupInvitations{

	@SerializedName("_id")
	private String id;

	@SerializedName("pendingInviteRequest")
	private List<PendingInviteRequestItem> pendingInviteRequest;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPendingInviteRequest(List<PendingInviteRequestItem> pendingInviteRequest){
		this.pendingInviteRequest = pendingInviteRequest;
	}

	public List<PendingInviteRequestItem> getPendingInviteRequest(){
		return pendingInviteRequest;
	}

	@Override
 	public String toString(){
		return 
			"GroupInvitations{" + 
			"_id = '" + id + '\'' + 
			",pendingInviteRequest = '" + pendingInviteRequest + '\'' + 
			"}";
		}
}
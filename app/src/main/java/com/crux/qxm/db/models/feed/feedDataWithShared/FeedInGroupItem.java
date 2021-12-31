package com.crux.qxm.db.models.feed.feedDataWithShared;

import com.google.gson.annotations.SerializedName;

public class FeedInGroupItem{

	@SerializedName("addedBy")
	private AddedBy addedBy;

	@SerializedName("groupId")
	private String groupId;

	@SerializedName("_id")
	private String id;

	@SerializedName("group")
	private String group;

	public void setAddedBy(AddedBy addedBy){
		this.addedBy = addedBy;
	}

	public AddedBy getAddedBy(){
		return addedBy;
	}

	public void setGroupId(String groupId){
		this.groupId = groupId;
	}

	public String getGroupId(){
		return groupId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setGroup(String group){
		this.group = group;
	}

	public String getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"FeedInGroupItem{" + 
			"addedBy = '" + addedBy + '\'' + 
			",groupId = '" + groupId + '\'' + 
			",_id = '" + id + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}
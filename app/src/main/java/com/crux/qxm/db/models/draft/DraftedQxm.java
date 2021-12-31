package com.crux.qxm.db.models.draft;

import com.google.gson.annotations.SerializedName;

public class DraftedQxm{

	@SerializedName("feedQxmId")
	private String feedQxmId;

	@SerializedName("feedCreationTime")
	private String feedCreationTime;

	@SerializedName("_id")
	private String id;

	@SerializedName("feedDescription")
	private String feedDescription;

	@SerializedName("feedTitle")
	private String feedTitle;

	public void setFeedQxmId(String feedQxmId){
		this.feedQxmId = feedQxmId;
	}

	public String getFeedQxmId(){
		return feedQxmId;
	}

	public void setFeedCreationTime(String feedCreationTime){
		this.feedCreationTime = feedCreationTime;
	}

	public String getFeedCreationTime(){
		return feedCreationTime;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFeedDescription(String feedDescription){
		this.feedDescription = feedDescription;
	}

	public String getFeedDescription(){
		return feedDescription;
	}

	public void setFeedTitle(String feedTitle){
		this.feedTitle = feedTitle;
	}

	public String getFeedTitle(){
		return feedTitle;
	}

	@Override
 	public String toString(){
		return 
			"DraftedQxm{" + 
			"feedQxmId = '" + feedQxmId + '\'' + 
			",feedCreationTime = '" + feedCreationTime + '\'' + 
			",_id = '" + id + '\'' + 
			",feedDescription = '" + feedDescription + '\'' + 
			",feedTitle = '" + feedTitle + '\'' + 
			"}";
		}
}
package com.crux.qxm.db.models.evaluation;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EvaluationPendingQxm {

	@SerializedName("feedParticipantsQuantity")
	private int feedParticipantsQuantity;

	@SerializedName("feedQxmId")
	private String feedQxmId;

	@SerializedName("feedCreationTime")
	private String feedCreationTime;

	@SerializedName("feedCategory")
	private List<String> feedCategory;

	@SerializedName("feedPendingEvaluationQuantity")
	private int feedPendingEvaluationQuantity;

	@SerializedName("feedTitle")
	private String feedTitle;

	@SerializedName("feedPrivacy")
	private String feedPrivacy;

	public void setFeedParticipantsQuantity(int feedParticipantsQuantity){
		this.feedParticipantsQuantity = feedParticipantsQuantity;
	}

	public int getFeedParticipantsQuantity(){
		return feedParticipantsQuantity;
	}

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

	public void setFeedCategory(List<String> feedCategory){
		this.feedCategory = feedCategory;
	}

	public List<String> getFeedCategory(){
		return feedCategory;
	}

	public void setFeedPendingEvaluationQuantity(int feedPendingEvaluationQuantity){
		this.feedPendingEvaluationQuantity = feedPendingEvaluationQuantity;
	}

	public int getFeedPendingEvaluationQuantity(){
		return feedPendingEvaluationQuantity;
	}

	public void setFeedTitle(String feedTitle){
		this.feedTitle = feedTitle;
	}

	public String getFeedTitle(){
		return feedTitle;
	}

	public void setFeedPrivacy(String feedPrivacy){
		this.feedPrivacy = feedPrivacy;
	}

	public String getFeedPrivacy(){
		return feedPrivacy;
	}

	@Override
 	public String toString(){
		return 
			"EvaluationPendingQxm{" +
			"feedParticipantsQuantity = '" + feedParticipantsQuantity + '\'' + 
			",feedQxmId = '" + feedQxmId + '\'' + 
			",feedCreationTime = '" + feedCreationTime + '\'' + 
			",feedCategory = '" + feedCategory + '\'' + 
			",feedPendingEvaluationQuantity = '" + feedPendingEvaluationQuantity + '\'' + 
			",feedTitle = '" + feedTitle + '\'' + 
			",feedPrivacy = '" + feedPrivacy + '\'' + 
			"}";
		}
}
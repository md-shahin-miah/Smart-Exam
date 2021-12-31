package com.crux.qxm.db.models.myQxmResult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class MyQxmResult{

	@SerializedName("qxmId")
	private String qxmId;

	@SerializedName("qxmCreatorId")
	private String qxmCreatorId;

	@SerializedName("qxmCategory")
	private List<String> qxmCategory;

	@SerializedName("userPerformance")
	private UserPerformance userPerformance;

	@SerializedName("qxmCreatorfullName")
	private String qxmCreatorfullName;

	@SerializedName("qxmCreatorProfileImage")
	private String qxmCreatorProfileImage;

	@SerializedName("qxmPrivacy")
	private String qxmPrivacy;

	@SerializedName("_id")
	private String id;

	@SerializedName("qxmName")
	private String qxmName;

	@SerializedName("qxmCreationTime")
	private String qxmCreationTime;

	@SerializedName("feedViewType")
	private  String  feedViewType;

	public String getQxmId() {
		return qxmId;
	}

	public void setQxmId(String qxmId) {
		this.qxmId = qxmId;
	}

	public void setQxmCategory(List<String> qxmCategory){
		this.qxmCategory = qxmCategory;
	}

	public List<String> getQxmCategory(){
		return qxmCategory;
	}

	public void setQxmCreatorId(String qxmCreatorId){
		this.qxmCreatorId = qxmCreatorId;
	}

	public String getQxmCreatorId(){
		return qxmCreatorId;
	}

	public void setUserPerformance(UserPerformance userPerformance){
		this.userPerformance = userPerformance;
	}

	public UserPerformance getUserPerformance(){
		return userPerformance;
	}

	public void setQxmCreatorfullName(String qxmCreatorfullName){
		this.qxmCreatorfullName = qxmCreatorfullName;
	}

	public String getQxmCreatorfullName(){
		return qxmCreatorfullName;
	}

	public void setQxmCreatorProfileImage(String qxmCreatorProfileImage){
		this.qxmCreatorProfileImage = qxmCreatorProfileImage;
	}

	public String getQxmCreatorProfileImage(){
		return qxmCreatorProfileImage;
	}
	public String getModifiedQxmCreatorProfileImage(){
		if(qxmCreatorProfileImage!=null){
			// as image might be from facebook or google as through social login
			if (qxmCreatorProfileImage.contains("facebook") || qxmCreatorProfileImage.contains("googleusercontent"))
				return qxmCreatorProfileImage;

			return IMAGE_SERVER_ROOT + qxmCreatorProfileImage;
		}
		return "";
	}
	public void setQxmPrivacy(String qxmPrivacy){
		this.qxmPrivacy = qxmPrivacy;
	}

	public String getQxmPrivacy(){
		return qxmPrivacy;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setQxmName(String qxmName){
		this.qxmName = qxmName;
	}

	public String getQxmName(){
		return qxmName;
	}

	public void setQxmCreationTime(String qxmCreationTime){
		this.qxmCreationTime = qxmCreationTime;
	}

	public String getQxmCreationTime(){
		return qxmCreationTime;
	}

	public String getFeedViewType() {
		return feedViewType;
	}

	public void setFeedViewType(String feedViewType) {
		this.feedViewType = feedViewType;
	}

	@Override
	public String toString() {
		return "MyQxmResult{" +
				"qxmId='" + qxmId + '\'' +
				", qxmCreatorId='" + qxmCreatorId + '\'' +
				", qxmCategory=" + qxmCategory +
				", userPerformance=" + userPerformance +
				", qxmCreatorfullName='" + qxmCreatorfullName + '\'' +
				", qxmCreatorProfileImage='" + qxmCreatorProfileImage + '\'' +
				", qxmPrivacy='" + qxmPrivacy + '\'' +
				", id='" + id + '\'' +
				", qxmName='" + qxmName + '\'' +
				", qxmCreationTime='" + qxmCreationTime + '\'' +
				", feedViewType='" + feedViewType + '\'' +
				'}';
	}
}
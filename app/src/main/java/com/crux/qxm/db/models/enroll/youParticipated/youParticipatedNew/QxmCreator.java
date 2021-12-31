package com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class QxmCreator {

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("_id")
	private String id;

	@SerializedName("profileImage")
	private String profileImage;

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}
	public String getModifiedProfileImage(){
		if(profileImage!=null){
			// as image might be from facebook or google as through social login
			if (profileImage.contains("facebook") || profileImage.contains("googleusercontent"))
				return profileImage;

			return IMAGE_SERVER_ROOT + profileImage;
		}
		return "";
	}

	@Override
 	public String toString(){
		return 
			"QxmCreatorId{" + 
			"fullName = '" + fullName + '\'' + 
			",_id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}
}
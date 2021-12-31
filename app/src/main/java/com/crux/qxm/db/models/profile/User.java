package com.crux.qxm.db.models.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;


public class User implements Parcelable {


	@SerializedName("_id")
	private String id;

	@SerializedName("fullName")
	private String fullName;

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
			"User{" + 
			"fullName = '" + fullName + '\'' + 
			",_id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.fullName);
		dest.writeString(this.id);
		dest.writeString(this.profileImage);
	}

	public User() {
	}

	protected User(Parcel in) {
		this.fullName = in.readString();
		this.id = in.readString();
		this.profileImage = in.readString();
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
package com.crux.qxm.db.models.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("userId")
	private String userId;

	@SerializedName("email")
	private String email;

	@SerializedName("token")
	private String token;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"createdAt = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",userId = '" + userId + '\'' + 
			",email = '" + email + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.createdAt);
		dest.writeString(this.id);
		dest.writeString(this.userId);
		dest.writeString(this.email);
		dest.writeString(this.token);
	}

	public Data() {
	}

	protected Data(Parcel in) {
		this.createdAt = in.readString();
		this.id = in.readString();
		this.userId = in.readString();
		this.email = in.readString();
		this.token = in.readString();
	}

	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel source) {
			return new Data(source);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};
}
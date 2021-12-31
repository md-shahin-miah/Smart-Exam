package com.crux.qxm.db.models.group.groupNotification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class GroupNotificationModel implements Parcelable {

	@SerializedName("_id")
	private String id;

	@SerializedName("user")
	private User user;

	@SerializedName("status")
	private String status;

	@SerializedName("group")
	private Group group;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
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
			"GroupNotificationModel{" + 
			"_id = '" + id + '\'' + 
			",user = '" + user + '\'' + 
			",status = '" + status + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeParcelable(this.user, flags);
		dest.writeString(this.status);
		dest.writeParcelable(this.group, flags);
	}

	public GroupNotificationModel() {
	}

	protected GroupNotificationModel(Parcel in) {
		this.id = in.readString();
		this.user = in.readParcelable(User.class.getClassLoader());
		this.status = in.readString();
		this.group = in.readParcelable(Group.class.getClassLoader());
	}

	public static final Parcelable.Creator<GroupNotificationModel> CREATOR = new Parcelable.Creator<GroupNotificationModel>() {
		@Override
		public GroupNotificationModel createFromParcel(Parcel source) {
			return new GroupNotificationModel(source);
		}

		@Override
		public GroupNotificationModel[] newArray(int size) {
			return new GroupNotificationModel[size];
		}
	};
}
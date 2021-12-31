package com.crux.qxm.db.models.group.groupNotification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Group implements Parcelable {

	@SerializedName("_id")
	private String id;

	@SerializedName("groupInfo")
	private GroupInfo groupInfo;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setGroupInfo(GroupInfo groupInfo){
		this.groupInfo = groupInfo;
	}

	public GroupInfo getGroupInfo(){
		return groupInfo;
	}

	@Override
 	public String toString(){
		return 
			"Group{" + 
			"_id = '" + id + '\'' + 
			",groupInfo = '" + groupInfo + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeParcelable(this.groupInfo, flags);
	}

	public Group() {
	}

	protected Group(Parcel in) {
		this.id = in.readString();
		this.groupInfo = in.readParcelable(GroupInfo.class.getClassLoader());
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		@Override
		public Group createFromParcel(Parcel source) {
			return new Group(source);
		}

		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	};
}
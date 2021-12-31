package com.crux.qxm.db.models.myQxm.list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class ListSettings implements Parcelable {

	@SerializedName("listPrivacy")
	private String listPrivacy;

	@SerializedName("listName")
	private String listName;


	public void setListPrivacy(String listPrivacy){
		this.listPrivacy = listPrivacy;
	}

	public String getListPrivacy(){
		return listPrivacy;
	}

	public void setListName(String listName){
		this.listName = listName;
	}

	public String getListName(){
		return listName;
	}

	@Override
 	public String toString(){
		return 
			"ListSettings{" + 
			"listPrivacy = '" + listPrivacy + '\'' + 
			",listName = '" + listName + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.listPrivacy);
		dest.writeString(this.listName);
	}

	public ListSettings() {
	}

	protected ListSettings(Parcel in) {
		this.listPrivacy = in.readString();
		this.listName = in.readString();
	}

	public static final Parcelable.Creator<ListSettings> CREATOR = new Parcelable.Creator<ListSettings>() {
		@Override
		public ListSettings createFromParcel(Parcel source) {
			return new ListSettings(source);
		}

		@Override
		public ListSettings[] newArray(int size) {
			return new ListSettings[size];
		}
	};
}
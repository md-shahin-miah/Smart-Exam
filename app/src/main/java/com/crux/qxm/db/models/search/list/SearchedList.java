package com.crux.qxm.db.models.search.list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class SearchedList implements Parcelable {

	@SerializedName("listPrivacy")
	private String listPrivacy;

	@SerializedName("_id")
	private String id;

	@SerializedName("listName")
	private String listName;

	public void setListPrivacy(String listPrivacy){
		this.listPrivacy = listPrivacy;
	}

	public String getListPrivacy(){
		return listPrivacy;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
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
			"MyCreatedList{" + 
			"listPrivacy = '" + listPrivacy + '\'' + 
			",_id = '" + id + '\'' + 
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
		dest.writeString(this.id);
		dest.writeString(this.listName);
	}

	public SearchedList() {
	}

	protected SearchedList(Parcel in) {
		this.listPrivacy = in.readString();
		this.id = in.readString();
		this.listName = in.readString();
	}

	public static final Parcelable.Creator<SearchedList> CREATOR = new Parcelable.Creator<SearchedList>() {
		@Override
		public SearchedList createFromParcel(Parcel source) {
			return new SearchedList(source);
		}

		@Override
		public SearchedList[] newArray(int size) {
			return new SearchedList[size];
		}
	};
}
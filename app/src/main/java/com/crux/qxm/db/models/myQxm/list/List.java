package com.crux.qxm.db.models.myQxm.list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class List implements Parcelable {

	@SerializedName("_id")
	private String id;

	@SerializedName("listSettings")
	private ListSettings listSettings;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setListSettings(ListSettings listSettings){
		this.listSettings = listSettings;
	}

	public ListSettings getListSettings(){
		return listSettings;
	}

	@Override
 	public String toString(){
		return 
			"List{" + 
			"_id = '" + id + '\'' + 
			",listSettings = '" + listSettings + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeParcelable(this.listSettings, flags);
	}

	public List() {
	}

	protected List(Parcel in) {
		this.id = in.readString();
		this.listSettings = in.readParcelable(ListSettings.class.getClassLoader());
	}

	public static final Parcelable.Creator<List> CREATOR = new Parcelable.Creator<List>() {
		@Override
		public List createFromParcel(Parcel source) {
			return new List(source);
		}

		@Override
		public List[] newArray(int size) {
			return new List[size];
		}
	};
}
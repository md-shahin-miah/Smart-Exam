package com.crux.qxm.db.models.poll;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class PollOptionsItem extends com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem implements Parcelable {

	@SerializedName("title")
	private String title;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"PollOptionsItem{" + 
			"title = '" + title + '\'' + 
			"}";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
	}

	public PollOptionsItem() {
	}

	public PollOptionsItem(String title) {
		this.title = title;
	}

	protected PollOptionsItem(Parcel in) {
		this.title = in.readString();
	}

	public static final Parcelable.Creator<PollOptionsItem> CREATOR = new Parcelable.Creator<PollOptionsItem>() {
		@Override
		public PollOptionsItem createFromParcel(Parcel source) {
			return new PollOptionsItem(source);
		}

		@Override
		public PollOptionsItem[] newArray(int size) {
			return new PollOptionsItem[size];
		}
	};
}
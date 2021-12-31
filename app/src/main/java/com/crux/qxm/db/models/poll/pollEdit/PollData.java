package com.crux.qxm.db.models.poll.pollEdit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PollData implements Parcelable {

	@SerializedName("pollName")
	private String pollName;

	@SerializedName("pollType")
	private String pollType;

	@SerializedName("_id")
	private String id;

	@SerializedName("pollOptions")
	private List<PollOptionsItem> pollOptions;

	@SerializedName("thumbnailURL")
	private String thumbnailURL;

	@SerializedName("sharedOn")
	private List<Object> sharedOn;

	public void setPollName(String pollName){
		this.pollName = pollName;
	}

	public String getPollName(){
		return pollName;
	}

	public void setPollType(String pollType){
		this.pollType = pollType;
	}

	public String getPollType(){
		return pollType;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPollOptions(List<PollOptionsItem> pollOptions){
		this.pollOptions = pollOptions;
	}

	public List<PollOptionsItem> getPollOptions(){
		return pollOptions;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	public void setSharedOn(List<Object> sharedOn){
		this.sharedOn = sharedOn;
	}

	public List<Object> getSharedOn(){
		return sharedOn;
	}

	@Override
 	public String toString(){
		return 
			"PollData{" + 
			"pollName = '" + pollName + '\'' + 
			",pollType = '" + pollType + '\'' + 
			",_id = '" + id + '\'' + 
			",pollOptions = '" + pollOptions + '\'' + 
			",thumbnailURL = '" + thumbnailURL + '\'' + 
			",sharedOn = '" + sharedOn + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.pollName);
		dest.writeString(this.pollType);
		dest.writeString(this.id);
		dest.writeList(this.pollOptions);
		dest.writeString(this.thumbnailURL);
		dest.writeList(this.sharedOn);
	}

	public PollData() {
	}

	protected PollData(Parcel in) {
		this.pollName = in.readString();
		this.pollType = in.readString();
		this.id = in.readString();
		this.pollOptions = new ArrayList<PollOptionsItem>();
		in.readList(this.pollOptions, PollOptionsItem.class.getClassLoader());
		this.thumbnailURL = in.readString();
		this.sharedOn = new ArrayList<Object>();
		in.readList(this.sharedOn, Object.class.getClassLoader());
	}

	public static final Parcelable.Creator<PollData> CREATOR = new Parcelable.Creator<PollData>() {
		@Override
		public PollData createFromParcel(Parcel source) {
			return new PollData(source);
		}

		@Override
		public PollData[] newArray(int size) {
			return new PollData[size];
		}
	};
}
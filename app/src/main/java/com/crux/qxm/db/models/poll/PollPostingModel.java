package com.crux.qxm.db.models.poll;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollPostingModel implements Parcelable {

	@SerializedName("_id")
	private String id;

	@SerializedName("pollName")
	private String pollName;

	@SerializedName("pollPrivacy")
	private String pollPrivacy;

	@SerializedName("pollType")
	private String pollType;

	@SerializedName("pollOptions")
	private List<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> pollOptions;

	@SerializedName("sharedOn")
	private String sharedOn;

	@SerializedName("thumbnailURL")
	private String thumbnailURL = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPollName(String pollName){
		this.pollName = pollName;
	}

	public String getPollName(){
		return pollName;
	}

	public void setPollPrivacy(String pollPrivacy){
		this.pollPrivacy = pollPrivacy;
	}

	public String getPollPrivacy(){
		return pollPrivacy;
	}

	public void setPollType(String pollType){
		this.pollType = pollType;
	}

	public String getPollType(){
		return pollType;
	}

	public void setPollOptions(List<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> pollOptions){
		this.pollOptions = pollOptions;
	}

	public List<com.crux.qxm.db.models.poll.pollEdit.PollOptionsItem> getPollOptions(){
		return pollOptions;
	}

	public void setSharedOn(String sharedOn){
		this.sharedOn = sharedOn;
	}

	public String getSharedOn(){
		return sharedOn;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	@Override
	public String toString() {
		return "PollPostingModel{" +
				"id='" + id + '\'' +
				", pollName='" + pollName + '\'' +
				", pollPrivacy='" + pollPrivacy + '\'' +
				", pollType='" + pollType + '\'' +
				", pollOptions=" + pollOptions +
				", sharedOn='" + sharedOn + '\'' +
				", thumbnailURL='" + thumbnailURL + '\'' +
				'}';
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.pollName);
		dest.writeString(this.pollPrivacy);
		dest.writeString(this.pollType);
		dest.writeTypedList(this.pollOptions);
		dest.writeString(this.sharedOn);
		dest.writeString(this.thumbnailURL);
	}

	public PollPostingModel() {
	}

	protected PollPostingModel(Parcel in) {
		this.id = in.readString();
		this.pollName = in.readString();
		this.pollPrivacy = in.readString();
		this.pollType = in.readString();
		this.pollOptions = in.createTypedArrayList(PollOptionsItem.CREATOR);
		this.sharedOn = in.readString();
		this.thumbnailURL = in.readString();
	}

	public static final Creator<PollPostingModel> CREATOR = new Creator<PollPostingModel>() {
		@Override
		public PollPostingModel createFromParcel(Parcel source) {
			return new PollPostingModel(source);
		}

		@Override
		public PollPostingModel[] newArray(int size) {
			return new PollPostingModel[size];
		}
	};
}
package com.crux.qxm.db.models.poll.pollEdit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PollOptionsItem implements Parcelable {

	@SerializedName("voteNum")
	private String voteNum;

	@SerializedName("_id")
	private String id;

	@SerializedName("voter")
	private List<Object> voter;

	@SerializedName("title")
	private String title;

	public PollOptionsItem(String title){

		this.title = title;
	}

	public void setVoteNum(String voteNum){
		this.voteNum = voteNum;
	}

	public String getVoteNum(){
		return voteNum;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setVoter(List<Object> voter){
		this.voter = voter;
	}

	public List<Object> getVoter(){
		return voter;
	}

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
			"voteNum = '" + voteNum + '\'' + 
			",_id = '" + id + '\'' + 
			",voter = '" + voter + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.voteNum);
		dest.writeString(this.id);
		dest.writeList(this.voter);
		dest.writeString(this.title);
	}

	public PollOptionsItem() {
	}

	protected PollOptionsItem(Parcel in) {
		this.voteNum = in.readString();
		this.id = in.readString();
		this.voter = new ArrayList<Object>();
		in.readList(this.voter, Object.class.getClassLoader());
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
package com.crux.qxm.db.models.search.singleMCQ;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class SearchedSingleMcq implements Parcelable {

	@SerializedName("feedQxmId")
	private String feedQxmId;

	@SerializedName("feedThumbnailURL")
	private String feedThumbnailURL;

	@SerializedName("feedCreatorName")
	private String feedCreatorName;

	@SerializedName("feedTitle")
	private String feedTitle;

	public void setFeedQxmId(String feedQxmId){
		this.feedQxmId = feedQxmId;
	}

	public String getFeedQxmId(){
		return feedQxmId;
	}

	public void setFeedThumbnailURL(String feedThumbnailURL){
		this.feedThumbnailURL = feedThumbnailURL;
	}

	public String getFeedThumbnailURL(){
		return feedThumbnailURL;
	}

	public void setFeedCreatorName(String feedCreatorName){
		this.feedCreatorName = feedCreatorName;
	}

	public String getFeedCreatorName(){
		return feedCreatorName;
	}

	public void setFeedTitle(String feedTitle){
		this.feedTitle = feedTitle;
	}

	public String getFeedTitle(){
		return feedTitle;
	}

	@Override
 	public String toString(){
		return 
			"SingleQxmFollowerItem{" + 
			"feedQxmId = '" + feedQxmId + '\'' + 
			",feedThumbnailURL = '" + feedThumbnailURL + '\'' + 
			",feedCreatorName = '" + feedCreatorName + '\'' + 
			",feedTitle = '" + feedTitle + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.feedQxmId);
		dest.writeString(this.feedThumbnailURL);
		dest.writeString(this.feedCreatorName);
		dest.writeString(this.feedTitle);
	}

	public SearchedSingleMcq() {
	}

	protected SearchedSingleMcq(Parcel in) {
		this.feedQxmId = in.readString();
		this.feedThumbnailURL = in.readString();
		this.feedCreatorName = in.readString();
		this.feedTitle = in.readString();
	}

	public static final Parcelable.Creator<SearchedSingleMcq> CREATOR = new Parcelable.Creator<SearchedSingleMcq>() {
		@Override
		public SearchedSingleMcq createFromParcel(Parcel source) {
			return new SearchedSingleMcq(source);
		}

		@Override
		public SearchedSingleMcq[] newArray(int size) {
			return new SearchedSingleMcq[size];
		}
	};
}
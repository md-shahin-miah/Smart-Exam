package com.crux.qxm.db.models.search.poll;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SearchedPoll implements Parcelable {


	@SerializedName("_id")
	private String id;

	@SerializedName("feedThumbnailURL")
	private String feedThumbnailURL;

	@SerializedName("feedCreatorName")
	private String feedCreatorName;

	@SerializedName("feedTitle")
	private String feedTitle;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public String toString() {
		return "SearchedPoll{" +
				"id='" + id + '\'' +
				", feedThumbnailURL='" + feedThumbnailURL + '\'' +
				", feedCreatorName='" + feedCreatorName + '\'' +
				", feedTitle='" + feedTitle + '\'' +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.feedThumbnailURL);
		dest.writeString(this.feedCreatorName);
		dest.writeString(this.feedTitle);
	}

	public SearchedPoll() {
	}

	protected SearchedPoll(Parcel in) {
		this.id = in.readString();
		this.feedThumbnailURL = in.readString();
		this.feedCreatorName = in.readString();
		this.feedTitle = in.readString();
	}

	public static final Parcelable.Creator<SearchedPoll> CREATOR = new Parcelable.Creator<SearchedPoll>() {
		@Override
		public SearchedPoll createFromParcel(Parcel source) {
			return new SearchedPoll(source);
		}

		@Override
		public SearchedPoll[] newArray(int size) {
			return new SearchedPoll[size];
		}
	};
}
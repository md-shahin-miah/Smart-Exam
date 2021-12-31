package com.crux.qxm.db.models.search.singleMCQ;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class SearchedSingleMcqContainer implements Parcelable {

	@SerializedName("singleQxmFollower")
	private List<SearchedSingleMcq> searchedFollowerSingleMcq;

	@SerializedName("otherSingleQxm")
	private List<SearchedSingleMcq> searchedOthersSingleMcq;

	public void setSearchedFollowerSingleMcq(List<SearchedSingleMcq> searchedFollowerSingleMcq){
		this.searchedFollowerSingleMcq = searchedFollowerSingleMcq;
	}

	public List<SearchedSingleMcq> getSearchedFollowerSingleMcq(){
		return searchedFollowerSingleMcq;
	}

	public void setSearchedOthersSingleMcq(List<SearchedSingleMcq> searchedOthersSingleMcq){
		this.searchedOthersSingleMcq = searchedOthersSingleMcq;
	}

	public List<SearchedSingleMcq> getSearchedOthersSingleMcq(){
		return searchedOthersSingleMcq;
	}

	@Override
 	public String toString(){
		return 
			"SearchSingleMcqContainer{" + 
			"singleMcqFollower = '" + searchedFollowerSingleMcq + '\'' +
			",singleMcqOther = '" + searchedOthersSingleMcq + '\'' +
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.searchedFollowerSingleMcq);
		dest.writeList(this.searchedOthersSingleMcq);
	}

	public SearchedSingleMcqContainer() {
	}

	protected SearchedSingleMcqContainer(Parcel in) {
		this.searchedFollowerSingleMcq = new ArrayList<SearchedSingleMcq>();
		in.readList(this.searchedFollowerSingleMcq, SearchedSingleMcq.class.getClassLoader());
		this.searchedOthersSingleMcq = new ArrayList<SearchedSingleMcq>();
		in.readList(this.searchedOthersSingleMcq, SearchedSingleMcq.class.getClassLoader());
	}

	public static final Parcelable.Creator<SearchedSingleMcqContainer> CREATOR = new Parcelable.Creator<SearchedSingleMcqContainer>() {
		@Override
		public SearchedSingleMcqContainer createFromParcel(Parcel source) {
			return new SearchedSingleMcqContainer(source);
		}

		@Override
		public SearchedSingleMcqContainer[] newArray(int size) {
			return new SearchedSingleMcqContainer[size];
		}
	};
}
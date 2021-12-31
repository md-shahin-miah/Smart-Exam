package com.crux.qxm.db.models.search.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SearchedGroup implements Parcelable {

    @SerializedName("_id")
    private String groupId;
    @SerializedName("groupInfo")
    private SearchedGroupInfo searchedGroupInfo;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public SearchedGroupInfo getSearchedGroupInfo() {
        return searchedGroupInfo;
    }

    public void setSearchedGroupInfo(SearchedGroupInfo searchedGroupInfo) {
        this.searchedGroupInfo = searchedGroupInfo;
    }

    @Override
    public String toString() {
        return "SearchedGroup{" +
                "groupId='" + groupId + '\'' +
                ", searchedGroupInfo=" + searchedGroupInfo +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeParcelable(this.searchedGroupInfo, flags);
    }

    public SearchedGroup() {
    }

    protected SearchedGroup(Parcel in) {
        this.groupId = in.readString();
        this.searchedGroupInfo = in.readParcelable(SearchedGroupInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchedGroup> CREATOR = new Parcelable.Creator<SearchedGroup>() {
        @Override
        public SearchedGroup createFromParcel(Parcel source) {
            return new SearchedGroup(source);
        }

        @Override
        public SearchedGroup[] newArray(int size) {
            return new SearchedGroup[size];
        }
    };
}

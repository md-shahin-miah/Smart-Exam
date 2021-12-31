package com.crux.qxm.db.models.search.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SearchedGroupInfo implements Parcelable {

    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupImage")
    private String groupThumbnail;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupThumbnail() {
        return groupThumbnail;
    }

    public void setGroupThumbnail(String groupThumbnail) {
        this.groupThumbnail = groupThumbnail;
    }

    @Override
    public String toString() {
        return "SearchedGroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupThumbnail='" + groupThumbnail + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeString(this.groupThumbnail);
    }

    public SearchedGroupInfo() {
    }

    protected SearchedGroupInfo(Parcel in) {
        this.groupName = in.readString();
        this.groupThumbnail = in.readString();
    }

    public static final Parcelable.Creator<SearchedGroupInfo> CREATOR = new Parcelable.Creator<SearchedGroupInfo>() {
        @Override
        public SearchedGroupInfo createFromParcel(Parcel source) {
            return new SearchedGroupInfo(source);
        }

        @Override
        public SearchedGroupInfo[] newArray(int size) {
            return new SearchedGroupInfo[size];
        }
    };
}

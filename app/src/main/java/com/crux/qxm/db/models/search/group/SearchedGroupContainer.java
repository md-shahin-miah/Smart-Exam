package com.crux.qxm.db.models.search.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchedGroupContainer implements Parcelable {

    @SerializedName("myGroup")
    private List<SearchedGroup> myGroupList;
    @SerializedName("otherGroup")
    private List<SearchedGroup> otherGroupList;

    public List<SearchedGroup> getMyGroupList() {
        return myGroupList;
    }

    public void setMyGroupList(List<SearchedGroup> myGroupList) {
        this.myGroupList = myGroupList;
    }

    public List<SearchedGroup> getOtherGroupList() {
        return otherGroupList;
    }

    public void setOtherGroupList(List<SearchedGroup> otherGroupList) {
        this.otherGroupList = otherGroupList;
    }

    @Override
    public String toString() {
        return "SearchedGroupContainer{" +
                "myGroupList=" + myGroupList +
                ", otherGroupList=" + otherGroupList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.myGroupList);
        dest.writeTypedList(this.otherGroupList);
    }

    public SearchedGroupContainer() {
    }

    protected SearchedGroupContainer(Parcel in) {
        this.myGroupList = in.createTypedArrayList(SearchedGroup.CREATOR);
        this.otherGroupList = in.createTypedArrayList(SearchedGroup.CREATOR);
    }

    public static final Parcelable.Creator<SearchedGroupContainer> CREATOR = new Parcelable.Creator<SearchedGroupContainer>() {
        @Override
        public SearchedGroupContainer createFromParcel(Parcel source) {
            return new SearchedGroupContainer(source);
        }

        @Override
        public SearchedGroupContainer[] newArray(int size) {
            return new SearchedGroupContainer[size];
        }
    };
}

package com.crux.qxm.db.models.search.qxm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchedQxmContainer implements Parcelable {

    @SerializedName("qxmFollower")
    private List<SearchedQxm> followerQxmList;
    @SerializedName("otherQxm")
    private List<SearchedQxm> otherQxmList;

    public List<SearchedQxm> getFollowerQxmList() {
        return followerQxmList;
    }

    public void setFollowerQxmList(List<SearchedQxm> followerQxmList) {
        this.followerQxmList = followerQxmList;
    }

    public List<SearchedQxm> getOtherQxmList() {
        return otherQxmList;
    }

    public void setOtherQxmList(List<SearchedQxm> otherQxmList) {
        this.otherQxmList = otherQxmList;
    }

    @Override
    public String toString() {
        return "SearchedQxmContainer{" +
                "followerQxmList=" + followerQxmList +
                ", otherQxmList=" + otherQxmList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.followerQxmList);
        dest.writeTypedList(this.otherQxmList);
    }

    public SearchedQxmContainer() {
    }

    protected SearchedQxmContainer(Parcel in) {
        this.followerQxmList = in.createTypedArrayList(SearchedQxm.CREATOR);
        this.otherQxmList = in.createTypedArrayList(SearchedQxm.CREATOR);
    }

    public static final Parcelable.Creator<SearchedQxmContainer> CREATOR = new Parcelable.Creator<SearchedQxmContainer>() {
        @Override
        public SearchedQxmContainer createFromParcel(Parcel source) {
            return new SearchedQxmContainer(source);
        }

        @Override
        public SearchedQxmContainer[] newArray(int size) {
            return new SearchedQxmContainer[size];
        }
    };
}

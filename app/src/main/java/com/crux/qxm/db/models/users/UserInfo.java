package com.crux.qxm.db.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.FeedData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo implements Parcelable {

    @SerializedName("profileData")
    private ProfileData profileData;
    @SerializedName("feed")
    private List<FeedData> feedDataList;


    public UserInfo() {
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public List<FeedData> getFeedDataList() {
        return feedDataList;
    }

    public void setFeedDataList(List<FeedData> feedDataList) {
        this.feedDataList = feedDataList;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "profileData=" + profileData +
                ", feedDataList=" + feedDataList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.profileData, flags);
        dest.writeTypedList(this.feedDataList);
    }

    protected UserInfo(Parcel in) {
        this.profileData = in.readParcelable(ProfileData.class.getClassLoader());
        this.feedDataList = in.createTypedArrayList(FeedData.CREATOR);
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}

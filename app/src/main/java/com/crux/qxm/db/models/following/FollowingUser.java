package com.crux.qxm.db.models.following;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FollowingUser implements Parcelable {
    @SerializedName("_id")
    private String followingId;
    @SerializedName("isOnline")
    private boolean isOnline;
    @SerializedName("notification")
    private String notification;

    @SerializedName("following")
    private Following following;

    public FollowingUser() {
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Following getFollowing() {
        return following;
    }

    public void setFollowing(Following following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "FollowingUser{" +
                "followingId='" + followingId + '\'' +
                ", isOnline=" + isOnline +
                ", notification='" + notification + '\'' +
                ", following=" + following +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.followingId);
        dest.writeByte(this.isOnline ? (byte) 1 : (byte) 0);
        dest.writeString(this.notification);
        dest.writeParcelable(this.following, flags);
    }

    protected FollowingUser(Parcel in) {
        this.followingId = in.readString();
        this.isOnline = in.readByte() != 0;
        this.notification = in.readString();
        this.following = in.readParcelable(Following.class.getClassLoader());
    }

    public static final Creator<FollowingUser> CREATOR = new Creator<FollowingUser>() {
        @Override
        public FollowingUser createFromParcel(Parcel source) {
            return new FollowingUser(source);
        }

        @Override
        public FollowingUser[] newArray(int size) {
            return new FollowingUser[size];
        }
    };
}

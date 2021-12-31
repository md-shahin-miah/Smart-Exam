
package com.crux.qxm.db.models.group.addMemberToGroup;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AddMemberList implements Parcelable {

    @SerializedName("following")
    public List<Following> following;
    @SerializedName("follower")
    public List<Follower> follower;

    public List<Following> getFollowing() {
        return following;
    }

    public void setFollowing(List<Following> following) {
        this.following = following;
    }

    public List<Follower> getFollower() {
        return follower;
    }

    public void setFollower(List<Follower> follower) {
        this.follower = follower;
    }

    @Override
    public String toString() {
        return "AddMemberList{" +
                "following=" + following +
                ", follower=" + follower +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.following);
        dest.writeList(this.follower);
    }

    public AddMemberList() {
    }

    protected AddMemberList(Parcel in) {
        this.following = new ArrayList<Following>();
        in.readList(this.following, Following.class.getClassLoader());
        this.follower = new ArrayList<Follower>();
        in.readList(this.follower, Follower.class.getClassLoader());
    }

    public static final Parcelable.Creator<AddMemberList> CREATOR = new Parcelable.Creator<AddMemberList>() {
        @Override
        public AddMemberList createFromParcel(Parcel source) {
            return new AddMemberList(source);
        }

        @Override
        public AddMemberList[] newArray(int size) {
            return new AddMemberList[size];
        }
    };
}

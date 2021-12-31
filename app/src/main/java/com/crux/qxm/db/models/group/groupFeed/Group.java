
package com.crux.qxm.db.models.group.groupFeed;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.FeedData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("groupInfo")
    private GroupInfo groupInfo;
    @SerializedName("memberCount")
    private String memberCount;
    @SerializedName("memberStatus")
    private String memberStatus;
    @SerializedName("myStatus")
    private String myStatus;
    @SerializedName("qxmCount")
    private String qxmCount;
    @SerializedName("groupFeed")
    private List<FeedData> groupFeed;

    public Group() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(String myStatus) {
        this.myStatus = myStatus;
    }

    public String getQxmCount() {
        return qxmCount;
    }

    public void setQxmCount(String qxmCount) {
        this.qxmCount = qxmCount;
    }

    public List<FeedData> getGroupFeed() {
        return groupFeed;
    }

    public void setGroupFeed(List<FeedData> groupFeed) {
        this.groupFeed = groupFeed;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", groupInfo=" + groupInfo +
                ", memberCount='" + memberCount + '\'' +
                ", memberStatus='" + memberStatus + '\'' +
                ", myStatus='" + myStatus + '\'' +
                ", qxmCount='" + qxmCount + '\'' +
                ", groupFeed=" + groupFeed +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.groupInfo, flags);
        dest.writeString(this.memberCount);
        dest.writeString(this.memberStatus);
        dest.writeString(this.myStatus);
        dest.writeString(this.qxmCount);
        dest.writeTypedList(this.groupFeed);
    }

    protected Group(Parcel in) {
        this.id = in.readString();
        this.groupInfo = in.readParcelable(GroupInfo.class.getClassLoader());
        this.memberCount = in.readString();
        this.memberStatus = in.readString();
        this.myStatus = in.readString();
        this.qxmCount = in.readString();
        this.groupFeed = in.createTypedArrayList(FeedData.CREATOR);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}

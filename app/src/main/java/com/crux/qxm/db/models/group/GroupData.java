package com.crux.qxm.db.models.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.FeedData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class GroupData implements Parcelable {
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupDescription")
    private String groupDescription;
    @SerializedName("groupImageUrl")
    private String groupImageUrl;
    @SerializedName("followerCount")
    private String followerCount;
    @SerializedName("memberCount")
    private String memberCount;
    @SerializedName("qxmCount")
    private String qxmCount;
    @SerializedName("myStatus")
    private String myStatus;
    @SerializedName("groupQxm")
    private List<FeedData> groupQxmList;

    public GroupData() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupImageUrl() {

        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getQxmCount() {
        return qxmCount;
    }

    public void setQxmCount(String qxmCount) {
        this.qxmCount = qxmCount;
    }

    public String getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(String myStatus) {
        this.myStatus = myStatus;
    }

    public List<FeedData> getGroupQxmList() {
        return groupQxmList;
    }

    public void setGroupQxmList(List<FeedData> groupQxmList) {
        this.groupQxmList = groupQxmList;
    }


    public String getModifiedGroupImageURL(){

        return IMAGE_SERVER_ROOT + groupImageUrl;
    }

    @Override
    public String toString() {
        return "GroupData{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupImageUrl='" + groupImageUrl + '\'' +
                ", followerCount='" + followerCount + '\'' +
                ", memberCount='" + memberCount + '\'' +
                ", qxmCount='" + qxmCount + '\'' +
                ", myStatus='" + myStatus + '\'' +
                ", groupQxmList=" + groupQxmList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeString(this.groupName);
        dest.writeString(this.groupDescription);
        dest.writeString(this.groupImageUrl);
        dest.writeString(this.followerCount);
        dest.writeString(this.memberCount);
        dest.writeString(this.qxmCount);
        dest.writeString(this.myStatus);
        dest.writeTypedList(this.groupQxmList);
    }

    protected GroupData(Parcel in) {
        this.groupId = in.readString();
        this.groupName = in.readString();
        this.groupDescription = in.readString();
        this.groupImageUrl = in.readString();
        this.followerCount = in.readString();
        this.memberCount = in.readString();
        this.qxmCount = in.readString();
        this.myStatus = in.readString();
        this.groupQxmList = in.createTypedArrayList(FeedData.CREATOR);
    }

    public static final Creator<GroupData> CREATOR = new Creator<GroupData>() {
        @Override
        public GroupData createFromParcel(Parcel source) {
            return new GroupData(source);
        }

        @Override
        public GroupData[] newArray(int size) {
            return new GroupData[size];
        }
    };
}

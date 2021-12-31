package com.crux.qxm.db.models.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Group implements Parcelable {
    @SerializedName("_id")
    private String groupId;
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupDescription")
    private String groupDescription;
    @SerializedName("groupImage")
    private String groupImageUrl;
    @SerializedName("groupMemberCount")
    private String groupMemberCount;
    @SerializedName("groupSettings")
    private GroupSettings groupSettings;

    public Group() {
    }

    public Group(GroupSettings groupSettings) {
        this.groupSettings = groupSettings;
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

    public String getGroupMemberCount() {
        return groupMemberCount;
    }

    public void setGroupMemberCount(String groupMemberCount) {
        this.groupMemberCount = groupMemberCount;
    }

    public GroupSettings getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(GroupSettings groupSettings) {
        this.groupSettings = groupSettings;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupImageUrl='" + groupImageUrl + '\'' +
                ", groupMemberCount='" + groupMemberCount + '\'' +
                ", groupSettings=" + groupSettings +
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
        dest.writeString(this.groupMemberCount);
        dest.writeParcelable(this.groupSettings, flags);
    }

    protected Group(Parcel in) {
        this.groupId = in.readString();
        this.groupName = in.readString();
        this.groupDescription = in.readString();
        this.groupImageUrl = in.readString();
        this.groupMemberCount = in.readString();
        this.groupSettings = in.readParcelable(GroupSettings.class.getClassLoader());
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

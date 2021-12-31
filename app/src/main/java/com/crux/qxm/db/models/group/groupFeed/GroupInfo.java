
package com.crux.qxm.db.models.group.groupFeed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class GroupInfo implements Parcelable {

    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupDescription")
    private String groupDescription;
    @SerializedName("groupImage")
    private String groupImageUrl;
    @SerializedName("groupSettings")
    private GroupSettings groupSettings;

    public GroupInfo() {
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

    public GroupSettings getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(GroupSettings groupSettings) {
        this.groupSettings = groupSettings;
    }

    public String getModifiedGroupImageUrl() {
        return IMAGE_SERVER_ROOT + groupImageUrl;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupImageUrl='" + groupImageUrl + '\'' +
                ", groupSettings=" + groupSettings +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeString(this.groupDescription);
        dest.writeString(this.groupImageUrl);
        dest.writeParcelable(this.groupSettings, flags);
    }

    protected GroupInfo(Parcel in) {
        this.groupName = in.readString();
        this.groupDescription = in.readString();
        this.groupImageUrl = in.readString();
        this.groupSettings = in.readParcelable(GroupSettings.class.getClassLoader());
    }

    public static final Creator<GroupInfo> CREATOR = new Creator<GroupInfo>() {
        @Override
        public GroupInfo createFromParcel(Parcel source) {
            return new GroupInfo(source);
        }

        @Override
        public GroupInfo[] newArray(int size) {
            return new GroupInfo[size];
        }
    };
}

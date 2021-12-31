package com.crux.qxm.db.models.menu.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.crux.qxm.utils.StaticValues.IMAGE_SERVER_ROOT;

public class GroupInfo implements Parcelable {

    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupImage")
    private String groupImage;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        if (groupImage != null && !groupImage.isEmpty())
            return IMAGE_SERVER_ROOT + groupImage;
        return groupImage;

    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupImage='" + groupImage + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeString(this.groupImage);
    }

    public GroupInfo() {
    }

    protected GroupInfo(Parcel in) {
        this.groupName = in.readString();
        this.groupImage = in.readString();
    }

    public static final Parcelable.Creator<GroupInfo> CREATOR = new Parcelable.Creator<GroupInfo>() {
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

package com.crux.qxm.db.models.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GroupSettings implements Parcelable {


    @SerializedName("groupSettings")
    private GroupSettings groupSettings;

    @SerializedName("isJoinRequestNeeded")
    private boolean isJoinRequestNeeded;

    @SerializedName("privacy")
    private String privacy;

    @SerializedName("isMemberWillVisble")
    private boolean isMemberVisibleToOther;

    @SerializedName("isMemberCanAddOther")
    private boolean isMemberCanAddOther;

    @SerializedName("notificationFrequency")
    private String notificationFrequency;

    public GroupSettings() {
    }

    public GroupSettings getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(GroupSettings groupSettings) {
        this.groupSettings = groupSettings;
    }

    public boolean isJoinRequestNeeded() {
        return isJoinRequestNeeded;
    }

    public void setJoinRequestNeeded(boolean joinRequestNeeded) {
        isJoinRequestNeeded = joinRequestNeeded;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public boolean isMemberVisibleToOther() {
        return isMemberVisibleToOther;
    }

    public void setMemberVisibleToOther(boolean memberVisibleToOther) {
        isMemberVisibleToOther = memberVisibleToOther;
    }

    public boolean isMemberCanAddOther() {
        return isMemberCanAddOther;
    }

    public void setMemberCanAddOther(boolean memberCanAddOther) {
        isMemberCanAddOther = memberCanAddOther;
    }

    public String getNotificationFrequency() {
        return notificationFrequency;
    }

    public void setNotificationFrequency(String notificationFrequency) {
        this.notificationFrequency = notificationFrequency;
    }

    @Override
    public String toString() {
        return "GroupSettings{" +
                "groupSettings=" + groupSettings +
                ", isJoinRequestNeeded=" + isJoinRequestNeeded +
                ", privacy='" + privacy + '\'' +
                ", isMemberVisibleToOther=" + isMemberVisibleToOther +
                ", isMemberCanAddOther=" + isMemberCanAddOther +
                ", notificationFrequency='" + notificationFrequency + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.groupSettings, flags);
        dest.writeByte(this.isJoinRequestNeeded ? (byte) 1 : (byte) 0);
        dest.writeString(this.privacy);
        dest.writeByte(this.isMemberVisibleToOther ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isMemberCanAddOther ? (byte) 1 : (byte) 0);
        dest.writeString(this.notificationFrequency);
    }

    protected GroupSettings(Parcel in) {
        this.groupSettings = in.readParcelable(GroupSettings.class.getClassLoader());
        this.isJoinRequestNeeded = in.readByte() != 0;
        this.privacy = in.readString();
        this.isMemberVisibleToOther = in.readByte() != 0;
        this.isMemberCanAddOther = in.readByte() != 0;
        this.notificationFrequency = in.readString();
    }

    public static final Creator<GroupSettings> CREATOR = new Creator<GroupSettings>() {
        @Override
        public GroupSettings createFromParcel(Parcel source) {
            return new GroupSettings(source);
        }

        @Override
        public GroupSettings[] newArray(int size) {
            return new GroupSettings[size];
        }
    };
}

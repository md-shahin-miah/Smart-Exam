package com.crux.qxm.db.models.group.groupFeed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GroupSettings implements Parcelable {
    @SerializedName("isJoinRequestNeeded")
    private boolean isJoinRequestNeeded;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("isMemberWillVisble")
    private boolean isMemberWillVisble;
    @SerializedName("isMemberCanAddOther")
    private boolean isMemberCanAddOther;
    @SerializedName("notificationFrequency")
    private String notificationFrequency;

    public void setIsJoinRequestNeeded(boolean isJoinRequestNeeded) {
        this.isJoinRequestNeeded = isJoinRequestNeeded;
    }

    public boolean isIsJoinRequestNeeded() {
        return isJoinRequestNeeded;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setIsMemberWillVisble(boolean isMemberWillVisble) {
        this.isMemberWillVisble = isMemberWillVisble;
    }

    public boolean isIsMemberWillVisble() {
        return isMemberWillVisble;
    }

    public void setIsMemberCanAddOther(boolean isMemberCanAddOther) {
        this.isMemberCanAddOther = isMemberCanAddOther;
    }

    public boolean isIsMemberCanAddOther() {
        return isMemberCanAddOther;
    }

    public void setNotificationFrequency(String notificationFrequency) {
        this.notificationFrequency = notificationFrequency;
    }

    public String getNotificationFrequency() {
        return notificationFrequency;
    }

    @Override
    public String toString() {
        return
                "GroupSettings{" +
                        "isJoinRequestNeeded = '" + isJoinRequestNeeded + '\'' +
                        ",privacy = '" + privacy + '\'' +
                        ",isMemberWillVisble = '" + isMemberWillVisble + '\'' +
                        ",isMemberCanAddOther = '" + isMemberCanAddOther + '\'' +
                        ",notificationFrequency = '" + notificationFrequency + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isJoinRequestNeeded ? (byte) 1 : (byte) 0);
        dest.writeString(this.privacy);
        dest.writeByte(this.isMemberWillVisble ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isMemberCanAddOther ? (byte) 1 : (byte) 0);
        dest.writeString(this.notificationFrequency);
    }

    public GroupSettings() {
    }

    protected GroupSettings(Parcel in) {
        this.isJoinRequestNeeded = in.readByte() != 0;
        this.privacy = in.readString();
        this.isMemberWillVisble = in.readByte() != 0;
        this.isMemberCanAddOther = in.readByte() != 0;
        this.notificationFrequency = in.readString();
    }

    public static final Parcelable.Creator<GroupSettings> CREATOR = new Parcelable.Creator<GroupSettings>() {
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

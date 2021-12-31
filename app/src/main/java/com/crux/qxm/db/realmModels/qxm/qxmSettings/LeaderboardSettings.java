package com.crux.qxm.db.realmModels.qxm.qxmSettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LeaderboardSettings extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    @SerializedName("publishType")
    private String publishType;
    @SerializedName("publishDate")
    private long publishDate;
    @SerializedName("privacy")
    private String privacy;

    public LeaderboardSettings() {
    }

    public LeaderboardSettings(String publishType, long publishDate, String privacy) {
        this.publishType = publishType;
        this.publishDate = publishDate;
        this.privacy = privacy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    @Override
    public String
    toString() {
        return "LeaderboardSettings{" +
                "id='" + id + '\'' +
                ", publishType='" + publishType + '\'' +
                ", publishDate=" + publishDate +
                ", privacy='" + privacy + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.publishType);
        dest.writeLong(this.publishDate);
        dest.writeString(this.privacy);
    }

    protected LeaderboardSettings(Parcel in) {
        this.id = in.readString();
        this.publishType = in.readString();
        this.publishDate = in.readLong();
        this.privacy = in.readString();
    }

    public static final Creator<LeaderboardSettings> CREATOR = new Creator<LeaderboardSettings>() {
        @Override
        public LeaderboardSettings createFromParcel(Parcel source) {
            return new LeaderboardSettings(source);
        }

        @Override
        public LeaderboardSettings[] newArray(int size) {
            return new LeaderboardSettings[size];
        }
    };
}

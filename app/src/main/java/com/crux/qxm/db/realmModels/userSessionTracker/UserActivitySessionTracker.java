package com.crux.qxm.db.realmModels.userSessionTracker;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserActivitySessionTracker extends RealmObject implements Parcelable {
    
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    @SerializedName("date")
    private String date;
    @SerializedName("loginActivitySession")
    private long loginActivitySession;
    @SerializedName("homeActivitySession")
    private long homeActivitySession;
    @SerializedName("createQxmActivitySession")
    private long createQxmActivitySession;
    @SerializedName("YouTubePlaybackActivitySession")
    private long YouTubePlaybackActivitySession;
    @SerializedName("totalActivitySession")
    private long totalActivitySession;
    private boolean isDataUploadedToServer;

    public UserActivitySessionTracker() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getLoginActivitySession() {
        return loginActivitySession;
    }

    public void setLoginActivitySession(long loginActivitySession) {
        this.loginActivitySession = loginActivitySession;
    }

    public long getHomeActivitySession() {
        return homeActivitySession;
    }

    public void setHomeActivitySession(long homeActivitySession) {
        this.homeActivitySession = homeActivitySession;
    }

    public long getCreateQxmActivitySession() {
        return createQxmActivitySession;
    }

    public void setCreateQxmActivitySession(long createQxmActivitySession) {
        this.createQxmActivitySession = createQxmActivitySession;
    }

    public long getYouTubePlaybackActivitySession() {
        return YouTubePlaybackActivitySession;
    }

    public void setYouTubePlaybackActivitySession(long youTubePlaybackActivitySession) {
        YouTubePlaybackActivitySession = youTubePlaybackActivitySession;
    }

    public long getTotalActivitySession() {
        return totalActivitySession;
    }

    public void setTotalActivitySession(long totalActivitySession) {
        this.totalActivitySession = totalActivitySession;
    }

    public boolean isDataUploadedToServer() {
        return isDataUploadedToServer;
    }

    public void setDataUploadedToServer(boolean dataUploadedToServer) {
        isDataUploadedToServer = dataUploadedToServer;
    }

    @Override
    public String toString() {
        return "UserActivitySessionTracker{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", loginActivitySession=" + loginActivitySession +
                ", homeActivitySession=" + homeActivitySession +
                ", createQxmActivitySession=" + createQxmActivitySession +
                ", YouTubePlaybackActivitySession=" + YouTubePlaybackActivitySession +
                ", totalActivitySession=" + totalActivitySession +
                ", isDataUploadedToServer=" + isDataUploadedToServer +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeLong(this.loginActivitySession);
        dest.writeLong(this.homeActivitySession);
        dest.writeLong(this.createQxmActivitySession);
        dest.writeLong(this.YouTubePlaybackActivitySession);
        dest.writeLong(this.totalActivitySession);
        dest.writeByte(this.isDataUploadedToServer ? (byte) 1 : (byte) 0);
    }

    protected UserActivitySessionTracker(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        this.loginActivitySession = in.readLong();
        this.homeActivitySession = in.readLong();
        this.createQxmActivitySession = in.readLong();
        this.YouTubePlaybackActivitySession = in.readLong();
        this.totalActivitySession = in.readLong();
        this.isDataUploadedToServer = in.readByte() != 0;
    }

    public static final Creator<UserActivitySessionTracker> CREATOR = new Creator<UserActivitySessionTracker>() {
        @Override
        public UserActivitySessionTracker createFromParcel(Parcel source) {
            return new UserActivitySessionTracker(source);
        }

        @Override
        public UserActivitySessionTracker[] newArray(int size) {
            return new UserActivitySessionTracker[size];
        }
    };
}

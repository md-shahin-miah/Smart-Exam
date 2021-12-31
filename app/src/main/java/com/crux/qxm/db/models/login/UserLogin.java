package com.crux.qxm.db.models.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.deviceInfo.DeviceInfo;
import com.google.gson.annotations.SerializedName;

public class UserLogin implements Parcelable {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("googleToken")
    private String googleToken;

    @SerializedName("fbToken")
    private String fbToken;

    @SerializedName("playersId")
    private String playersID;

    @SerializedName("deviceInfo")
    private DeviceInfo deviceInfo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getPlayersID() {
        return playersID;
    }

    public void setPlayersID(String playersID) {
        this.playersID = playersID;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", googleToken='" + googleToken + '\'' +
                ", fbToken='" + fbToken + '\'' +
                ", playersID='" + playersID + '\'' +
                ", deviceInfo=" + deviceInfo +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.googleToken);
        dest.writeString(this.fbToken);
        dest.writeString(this.playersID);
        dest.writeParcelable(this.deviceInfo, flags);
    }

    public UserLogin() {
    }

    protected UserLogin(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.googleToken = in.readString();
        this.fbToken = in.readString();
        this.playersID = in.readString();
        this.deviceInfo = in.readParcelable(DeviceInfo.class.getClassLoader());
    }

    public static final Creator<UserLogin> CREATOR = new Creator<UserLogin>() {
        @Override
        public UserLogin createFromParcel(Parcel source) {
            return new UserLogin(source);
        }

        @Override
        public UserLogin[] newArray(int size) {
            return new UserLogin[size];
        }
    };
}

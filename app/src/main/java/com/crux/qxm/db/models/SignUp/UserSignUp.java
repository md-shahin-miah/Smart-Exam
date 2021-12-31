package com.crux.qxm.db.models.SignUp;


import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.deviceInfo.DeviceInfo;
import com.google.gson.annotations.SerializedName;

public class UserSignUp implements Parcelable {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("playersId")
    private String playersID;

    @SerializedName("deviceInfo")
    private DeviceInfo deviceInfo;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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
        return "UserSignUp{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
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
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.fullName);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.playersID);
        dest.writeParcelable(this.deviceInfo, flags);
    }

    public UserSignUp() {
    }

    protected UserSignUp(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.fullName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.playersID = in.readString();
        this.deviceInfo = in.readParcelable(DeviceInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserSignUp> CREATOR = new Parcelable.Creator<UserSignUp>() {
        @Override
        public UserSignUp createFromParcel(Parcel source) {
            return new UserSignUp(source);
        }

        @Override
        public UserSignUp[] newArray(int size) {
            return new UserSignUp[size];
        }
    };
}

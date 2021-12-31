package com.crux.qxm.db.models.deviceInfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DeviceInfo implements Parcelable {
    @SerializedName("deviceOS")
    private String deviceOS;
    @SerializedName("deviceOSVersion")
    private String deviceOSVersion;
    @SerializedName("appVersion")
    private String appVersion;
    @SerializedName("deviceBrand")
    private String deviceBrand;
    @SerializedName("deviceModel")
    private String deviceModel;
    @SerializedName("deviceManufacturer")
    private String deviceManufacturer;
    @SerializedName("macAddress")
    private String macAddress;

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceOS='" + deviceOS + '\'' +
                ", deviceOSVersion='" + deviceOSVersion + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceBrand='" + deviceBrand + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceManufacturer='" + deviceManufacturer + '\'' +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceOS);
        dest.writeString(this.deviceOSVersion);
        dest.writeString(this.appVersion);
        dest.writeString(this.deviceBrand);
        dest.writeString(this.deviceModel);
        dest.writeString(this.deviceManufacturer);
        dest.writeString(this.macAddress);
    }

    public DeviceInfo() {
    }

    protected DeviceInfo(Parcel in) {
        this.deviceOS = in.readString();
        this.deviceOSVersion = in.readString();
        this.appVersion = in.readString();
        this.deviceBrand = in.readString();
        this.deviceModel = in.readString();
        this.deviceManufacturer = in.readString();
        this.macAddress = in.readString();
    }

    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel source) {
            return new DeviceInfo(source);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}


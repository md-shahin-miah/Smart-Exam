package com.crux.qxm.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.crux.qxm.db.models.deviceInfo.DeviceInfo;

import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeviceInfoHelper {
    private static final String TAG = "DeviceInfoHelper";

    public static DeviceInfo getDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo();
        try {
            deviceInfo.setDeviceOS("Android");
            deviceInfo.setDeviceOSVersion(Build.VERSION.RELEASE);
            deviceInfo.setAppVersion(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
            deviceInfo.setDeviceBrand(Build.BRAND);
            deviceInfo.setDeviceModel(Build.MODEL);
            deviceInfo.setDeviceManufacturer(Build.MANUFACTURER);
            deviceInfo.setMacAddress(getMacAddress());

        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "setAppVersion exception: " + Arrays.toString(e.getStackTrace()));
        }

        return deviceInfo;
    }

    private static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.d(TAG, "getMacAddress: exception: " + Arrays.toString(ex.getStackTrace()));
        }
        return "02:00:00:00:00:00";
    }


}

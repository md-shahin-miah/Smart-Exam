package com.crux.qxm.db.models.qxmSettings;

import com.google.gson.annotations.SerializedName;

public class SettingsDataContainer {

    @SerializedName("settingsData")
    private QxmAppNotificationSettings settingsData;


    public QxmAppNotificationSettings getSettingsData() {
        return settingsData;
    }

    public void setSettingsData(QxmAppNotificationSettings settingsData) {
        this.settingsData = settingsData;
    }

    @Override
    public String toString() {
        return "SettingsDataContainer{" +
                "settingsData=" + settingsData +
                '}';
    }
}

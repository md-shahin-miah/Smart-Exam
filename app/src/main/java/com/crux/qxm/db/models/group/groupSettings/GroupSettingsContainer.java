package com.crux.qxm.db.models.group.groupSettings;

import com.crux.qxm.db.models.group.GroupSettings;
import com.google.gson.annotations.SerializedName;

public class GroupSettingsContainer {
    @SerializedName("groupSettings")
    private GroupSettings groupSettings;

    public GroupSettings getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(GroupSettings groupSettings) {
        this.groupSettings = groupSettings;
    }

    @Override
    public String toString() {
        return "GroupSettingsContainer{" +
                "groupSettings=" + groupSettings +
                '}';
    }
}

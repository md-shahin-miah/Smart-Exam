package com.crux.qxm.db.models.activityLog;

import java.util.List;

public class ActivityLogDateContainer {

    private String date;
    private List<ActivitiesItem> activitiesItemList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ActivitiesItem> getActivitiesItemList() {
        return activitiesItemList;
    }

    public void setActivitiesItemList(List<ActivitiesItem> activitiesItemList) {
        this.activitiesItemList = activitiesItemList;
    }

    @Override
    public String toString() {
        return "ActivityLogDateContainer{" +
                "date='" + date + '\'' +
                ", activitiesItemList=" + activitiesItemList +
                '}';
    }
}

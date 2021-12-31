package com.crux.qxm.db.models.activityLog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityLog {

	@SerializedName("activities")
	private List<ActivitiesItem> activities;

	public void setActivities(List<ActivitiesItem> activities){
		this.activities = activities;
	}

	public List<ActivitiesItem> getActivities(){
		return activities;
	}

	@Override
 	public String toString(){
		return 
			"ActivityLog{" +
			"activities = '" + activities + '\'' + 
			"}";
		}
}
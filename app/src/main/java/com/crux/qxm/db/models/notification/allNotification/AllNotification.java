package com.crux.qxm.db.models.notification.allNotification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllNotification{

	@SerializedName("all")
	private List<NotificationItem> all;

	@SerializedName("_id")
	private String id;

	public List<NotificationItem> getAll() {
		return all;
	}

	public void setAll(List<NotificationItem> all) {
		this.all = all;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AllNotification{" +
				"all=" + all +
				", id='" + id + '\'' +
				'}';
	}
}
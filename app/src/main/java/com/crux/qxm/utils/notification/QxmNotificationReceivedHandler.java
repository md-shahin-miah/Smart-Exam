package com.crux.qxm.utils.notification;

import android.app.Application;
import android.util.Log;

import com.crux.qxm.App;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class QxmNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    private static final String TAG = "QxmNotificationReceived";
    private Application application;

    public QxmNotificationReceivedHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;

        Log.d(TAG, "notificationReceived: ");
        if (data != null) {
            Log.d(TAG, "notificationReceived: data not null");
            Log.d(TAG, "notificationReceived: data = " + data.toString());
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i(TAG, "customkey set with value: " + customKey);
        }
    }
}

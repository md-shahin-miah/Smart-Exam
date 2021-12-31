package com.crux.qxm.utils.notification;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.crux.qxm.views.activities.HomeActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import static com.crux.qxm.utils.StaticValues.CHANNEL_GROUP_INVITE_REQUEST_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_KEY;
import static com.crux.qxm.utils.StaticValues.CHANNEL_ENROLL_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_EVALUATION_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_FOLLOWER_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_GROUP_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_GROUP_JOIN_REQUEST_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_PRIVATE_QXM_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_QXM_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_RESULT_NAME;
import static com.crux.qxm.utils.StaticValues.GROUP_ID_KEY;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_FOLLOWER_FULL_NAME;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_FOLLOWER_ID;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_BUNDLE;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_GROUP_ID_KEY;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_GROUP_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_PARTICIPATION_ID;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_QXM_ID;

public class QxmNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    private static final String TAG = "QxmNotificationOpenedHa";
    private Application application;

    public QxmNotificationOpenedHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String channel;

        Log.d(TAG, "notificationOpened: " + result.toString());

        if (data != null) {
            Log.d(TAG, "notificationOpened: data = " + data.toString());

            channel = data.optString("channel", null);




            if (channel != null) {
                Bundle bundle = new Bundle();
                bundle.putString(CHANNEL_KEY, channel);

                switch (channel) {
                    case CHANNEL_FOLLOWER_NAME:
                        Log.d(TAG, "CHANNEL_FOLLOWER_NAME: ");
                        bundle.putString(NOTIFICATION_FOLLOWER_ID, data.optString(NOTIFICATION_FOLLOWER_ID, null));
                        bundle.putString(NOTIFICATION_FOLLOWER_FULL_NAME, data.optString(NOTIFICATION_FOLLOWER_FULL_NAME, null));
                        startApp(bundle);

                        break;
                    case CHANNEL_QXM_NAME:
                        Log.d(TAG, "CHANNEL_QXM_NAME: ");
                        bundle.putString(NOTIFICATION_QXM_ID, data.optString(NOTIFICATION_QXM_ID, null));
                        startApp(bundle);

                        break;
                    case CHANNEL_PRIVATE_QXM_NAME:
                        Log.d(TAG, "CHANNEL_PRIVATE_QXM_NAME: ");
                        startApp(bundle);

                        break;
                    case CHANNEL_EVALUATION_NAME:
                        Log.d(TAG, "CHANNEL_EVALUATION_NAME: ");

                        bundle.putString(NOTIFICATION_PARTICIPATION_ID, data.optString(NOTIFICATION_PARTICIPATION_ID, null));
                        startApp(bundle);

                        break;
                    case CHANNEL_RESULT_NAME:
                        Log.d(TAG, "CHANNEL_RESULT_NAME: ");
                        bundle.putString(NOTIFICATION_PARTICIPATION_ID, data.optString(NOTIFICATION_PARTICIPATION_ID, null));
                        startApp(bundle);

                        break;
                    case CHANNEL_GROUP_NAME:
                        Log.d(TAG, "CHANNEL_GROUP_NAME: ");
                        bundle.putString(NOTIFICATION_GROUP_ID_KEY, data.optString(NOTIFICATION_GROUP_ID_KEY, null));
                        bundle.putString(NOTIFICATION_GROUP_NAME_KEY, data.optString(NOTIFICATION_GROUP_NAME_KEY, null));
                        startApp(bundle);


                        break;
                    case CHANNEL_GROUP_JOIN_REQUEST_NAME:
                        Log.d(TAG, "CHANNEL_GROUP_JOIN_REQUEST_NAME: ");
                        bundle.putString(NOTIFICATION_GROUP_ID_KEY, data.optString(NOTIFICATION_GROUP_ID_KEY, null));
                        startApp(bundle);


                        break;
                    case CHANNEL_GROUP_INVITE_REQUEST_NAME:
                        Log.d(TAG, "CHANNEL_GROUP_INVITE_REQUEST_NAME: ");
                        //bundle.putString(NOTIFICATION_GROUP_ID_KEY, data.optString(NOTIFICATION_GROUP_ID_KEY, null));
                        startApp(bundle);


                        break;
                    case CHANNEL_ENROLL_NAME:
                        Log.d(TAG, "CHANNEL_ENROLL_NAME: ");
                        startApp(bundle);


                        break;
                }
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i(TAG, "notificationOpened: Button pressed with id: " + result.action.actionID);
        }
    }

    private void startApp(Bundle bundle) {
        Intent intent = new Intent(application, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(NOTIFICATION_BUNDLE, bundle);
        application.startActivity(intent);

    }

    private void openFragment(Bundle bundle){

    }
}

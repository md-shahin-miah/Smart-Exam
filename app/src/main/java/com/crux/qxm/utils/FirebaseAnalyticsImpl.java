package com.crux.qxm.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class FirebaseAnalyticsImpl {

    public static void logEvent(Context context,String keyName, String valueName){

        com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics =
                com.google.firebase.analytics.FirebaseAnalytics.getInstance(context);

        // making a space less name for passing in firebase analytics
        String spaceLessName = keyName.replaceAll(" ","_");
        String forwardSlashLessName = spaceLessName.replace("/","_");
        String hiphenLessName = forwardSlashLessName.replace("-","_");
        Log.d("FireBasePassingName",hiphenLessName);

        // fireBase analytics event tracking
        Bundle params = new Bundle();
        params.putString(keyName,valueName);
        firebaseAnalytics.logEvent(hiphenLessName, params);
    }
}

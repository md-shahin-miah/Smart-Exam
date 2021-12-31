package com.crux.qxm.utils.qxmLoginHelper;

import android.util.Log;

import com.crux.qxm.db.models.response.UserAuthenticationResponse;
import com.crux.qxm.db.realmService.RealmService;
import com.onesignal.OneSignal;

public class QxmLoginHelper {


    private static final String TAG = "QxmLoginHelper";
    private RealmService realmService;

    public QxmLoginHelper(RealmService realmService) {
        this.realmService = realmService;
    }

    public void qxmLoginSuccess(UserAuthenticationResponse user){
        OneSignal.setSubscription(true);
        OneSignal.sendTag("notification_tag",user.getUser().getUserId());
        Log.d(TAG, "qxmLoginSuccess notification_tag: "+user.getUser().getUserId());
        realmService.addUser(user.getUser());
        realmService.saveToken(user.getToken());
        realmService.setSignUpTriggered(false);
        realmService.resetUserActivitySessionTrackerData();
    }

    public void qxmLoginSuccess(UserAuthenticationResponse user,String imageUrl){

        OneSignal.setSubscription(true);
        realmService.addUser(user.getUser(),imageUrl);
        realmService.saveToken(user.getToken());

        realmService.setSignUpTriggered(false);
        realmService.resetUserActivitySessionTrackerData();
    }

    public void qxmSignUpSuccess(UserAuthenticationResponse user){

        OneSignal.setSubscription(true);
        OneSignal.sendTag("notification_tag",user.getUser().getUserId());
        realmService.addUser(user.getUser());
        realmService.saveToken(user.getToken());
        // save user sign up steps status
        realmService.setSignUpTriggered(true);
        realmService.resetUserActivitySessionTrackerData();

        Log.d(TAG, "qxmSignUpSuccess: Called");
        Log.d(TAG, "qxmSignUpSuccess SignUpTriggered: "+realmService.getSignUpStatus());
    }

}

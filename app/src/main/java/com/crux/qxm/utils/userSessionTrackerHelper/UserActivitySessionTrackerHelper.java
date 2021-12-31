package com.crux.qxm.utils.userSessionTrackerHelper;

import android.util.Log;

import com.crux.qxm.db.realmModels.userSessionTracker.UserActivitySessionTracker;
import com.crux.qxm.db.realmService.RealmService;

import io.realm.Realm;

public class UserActivitySessionTrackerHelper {
    private static final String TAG = "UserActivitySessionTrac";
    private RealmService realmService;

    public UserActivitySessionTrackerHelper() {
        realmService = new RealmService(Realm.getDefaultInstance());
    }

    public UserActivitySessionTrackerHelper(RealmService realmService) {
        this.realmService = realmService;
    }

    public UserActivitySessionTracker getActivitySessionTracker() {
        return realmService.getUserActivitySessionTrackerData();
    }

    public void saveLoginActivitySession(long startTime, long endTime) {
        Log.d(TAG, String.format("saveLoginActivitySession: startTime= %s, endTime= %s, session= %s",
                startTime, endTime, (endTime - startTime)));
        realmService.saveLoginActivitySession(endTime - startTime);
    }

    public void saveHomeActivitySession(long startTime, long endTime) {
        Log.d(TAG, String.format("saveHomeActivitySession: startTime= %s, endTime= %s, session= %s",
                startTime, endTime, (endTime - startTime)));
        realmService.saveHomeActivitySession(endTime - startTime);
    }

    public void saveCreateQxmActivitySession(long startTime, long endTime) {
        Log.d(TAG, String.format("saveCreateQxmActivitySession: startTime= %s, endTime= %s, session= %s",
                startTime, endTime, (endTime - startTime)));
        realmService.saveCreateQxmActivitySession(endTime - startTime);
    }

    public void saveYouTubePlayBackActivitySession(long startTime, long endTime) {
        Log.d(TAG, String.format("saveYouTubePlayBackActivitySession: startTime= %s, endTime= %s, session= %s",
                startTime, endTime, (endTime - startTime)));
        realmService.saveYouTubePlayBackActivitySession(endTime - startTime);
    }
}

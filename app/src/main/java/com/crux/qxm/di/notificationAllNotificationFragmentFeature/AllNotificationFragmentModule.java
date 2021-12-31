package com.crux.qxm.di.notificationAllNotificationFragmentFeature;

import com.crux.qxm.views.fragments.notification.AllNotificationFragment;

import dagger.Module;

@Module
public class AllNotificationFragmentModule {
    final AllNotificationFragment allNotificationFragment;

    public AllNotificationFragmentModule(AllNotificationFragment allNotificationFragment) {
        this.allNotificationFragment = allNotificationFragment;
    }
}

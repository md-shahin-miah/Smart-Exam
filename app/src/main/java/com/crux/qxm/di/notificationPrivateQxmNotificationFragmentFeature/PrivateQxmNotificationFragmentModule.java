package com.crux.qxm.di.notificationPrivateQxmNotificationFragmentFeature;

import com.crux.qxm.views.fragments.notification.PrivateQxmNotificationFragment;

import dagger.Module;

@Module
public class PrivateQxmNotificationFragmentModule {
    final PrivateQxmNotificationFragment privateQxmNotificationFragment;

    public PrivateQxmNotificationFragmentModule(PrivateQxmNotificationFragment privateQxmNotificationFragment) {
        this.privateQxmNotificationFragment = privateQxmNotificationFragment;
    }
}

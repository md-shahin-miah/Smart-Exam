package com.crux.qxm.di.notificationQxmNotificationFragmentFeature;

import com.crux.qxm.views.fragments.notification.QxmNotificationFragment;

import dagger.Module;

@Module
public class QxmNotificationFragmentModule {
    final QxmNotificationFragment qxmNotificationFragment;

    public QxmNotificationFragmentModule(QxmNotificationFragment qxmNotificationFragment) {
        this.qxmNotificationFragment = qxmNotificationFragment;
    }
}

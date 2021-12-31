package com.crux.qxm.di.notificationResultNotificationFragmentFeature;

import com.crux.qxm.views.fragments.notification.ResultNotificationFragment;

import dagger.Module;

@Module
public class ResultNotificationFragmentModule {

    final ResultNotificationFragment resultNotificationFragment;

    public ResultNotificationFragmentModule(ResultNotificationFragment resultNotificationFragment) {
        this.resultNotificationFragment = resultNotificationFragment;
    }
}

package com.crux.qxm.di.activityLogFragmentFeature;

import com.crux.qxm.views.fragments.acitivityLog.ActivityLogFragment;

import dagger.Module;

@Module
public class ActivityLogFragmentModule {

    final ActivityLogFragment activityLogFragment;

    public ActivityLogFragmentModule(ActivityLogFragment activityLogFragment) {
        this.activityLogFragment = activityLogFragment;
    }
}

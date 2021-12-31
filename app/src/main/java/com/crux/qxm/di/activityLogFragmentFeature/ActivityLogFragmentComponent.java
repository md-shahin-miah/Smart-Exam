package com.crux.qxm.di.activityLogFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.acitivityLog.ActivityLogFragment;

import dagger.Component;

@ActivityLogFragmentScope
@Component (modules = ActivityLogFragmentModule.class, dependencies = AppComponent.class)
public interface ActivityLogFragmentComponent {
    void injectActivityLogFragmentFeature(ActivityLogFragment activityLogFragment);
}


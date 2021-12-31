package com.crux.qxm.di.notificationResultNotificationFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.notification.ResultNotificationFragment;

import dagger.Component;

@ResultNotificationFragmentScope
@Component (modules = ResultNotificationFragmentModule.class, dependencies = AppComponent.class)
public interface ResultNotificationFragmentComponent {
    void injectResultNotificationFragmentFeature(ResultNotificationFragment resultNotificationFragment);
}

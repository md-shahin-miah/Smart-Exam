package com.crux.qxm.di.notificationAllNotificationFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.notification.AllNotificationFragment;

import dagger.Component;

@AllNotificationFragmentScope
@Component (modules = AllNotificationFragmentModule.class, dependencies = AppComponent.class)
public interface AllNotificationFragmentComponent {
    void injectAllNotificationFragmentFeature(AllNotificationFragment allNotificationFragment);
}

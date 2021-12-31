package com.crux.qxm.di.notificationPrivateQxmNotificationFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.notification.PrivateQxmNotificationFragment;

import dagger.Component;

@PrivateQxmNotificationFragmentScope
@Component(modules = PrivateQxmNotificationFragmentModule.class, dependencies = AppComponent.class)
public interface PrivateQxmNotificationFragmentComponent {
    void injectPrivateQxmNotificationFragmentFeature(PrivateQxmNotificationFragment privateQxmNotificationFragment);
}

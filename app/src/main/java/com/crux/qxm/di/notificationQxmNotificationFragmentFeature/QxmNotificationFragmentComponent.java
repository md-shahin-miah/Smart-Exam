package com.crux.qxm.di.notificationQxmNotificationFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.notification.QxmNotificationFragment;

import dagger.Component;

@QxmNotificationFragmentScope
@Component (modules = QxmNotificationFragmentModule.class, dependencies = AppComponent.class)
public interface QxmNotificationFragmentComponent {
    void injectQxmNotificationFragmentFeature(QxmNotificationFragment qxmNotificationFragment);
}

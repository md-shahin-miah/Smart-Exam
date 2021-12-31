package com.crux.qxm.di.notificationGroupNotificationFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.notification.GroupNotificationFragment;

import dagger.Component;

@GroupNotificationFragmentScope
@Component (modules = GroupNotificationFragmentModule.class, dependencies = AppComponent.class)
public interface GroupNotificationFragmentComponent {
    void injectGroupNotificationFragmentFeature(GroupNotificationFragment groupNotificationFragment
    );
}

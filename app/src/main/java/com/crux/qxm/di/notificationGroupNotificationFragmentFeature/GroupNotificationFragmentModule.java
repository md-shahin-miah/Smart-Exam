package com.crux.qxm.di.notificationGroupNotificationFragmentFeature;

import com.crux.qxm.views.fragments.notification.GroupNotificationFragment;

import dagger.Module;

@Module
public class GroupNotificationFragmentModule {
    final GroupNotificationFragment groupNotificationFragment;

    public GroupNotificationFragmentModule(GroupNotificationFragment groupNotificationFragment) {
        this.groupNotificationFragment = groupNotificationFragment;
    }
}

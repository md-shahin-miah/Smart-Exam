package com.crux.qxm.di.groupSettingsFeature;

import com.crux.qxm.views.fragments.group.GroupSettingsFragment;

import dagger.Module;

@Module
public class GroupSettingsModule {

    final GroupSettingsFragment groupSettingsFragment;

    public GroupSettingsModule(GroupSettingsFragment groupSettingsFragment) {
        this.groupSettingsFragment = groupSettingsFragment;
    }
}

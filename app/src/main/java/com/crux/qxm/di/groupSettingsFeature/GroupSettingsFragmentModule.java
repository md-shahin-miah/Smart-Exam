package com.crux.qxm.di.groupSettingsFeature;

import com.crux.qxm.views.fragments.group.GroupSettingsFragment;

import dagger.Module;

@Module
public class GroupSettingsFragmentModule {

    final GroupSettingsFragment groupSettingsFragment;

    public GroupSettingsFragmentModule(GroupSettingsFragment groupSettingsFragment) {
        this.groupSettingsFragment = groupSettingsFragment;
    }
}

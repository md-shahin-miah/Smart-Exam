package com.crux.qxm.di.profileSettingsFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.ProfileSettingsFragment;

import dagger.Module;

@Module
public class ProfileSettingsFragmentModule {

    private final ProfileSettingsFragment profileSettingsFragment;

    public ProfileSettingsFragmentModule(ProfileSettingsFragment profileSettingsFragment) {
        this.profileSettingsFragment = profileSettingsFragment;
    }
}

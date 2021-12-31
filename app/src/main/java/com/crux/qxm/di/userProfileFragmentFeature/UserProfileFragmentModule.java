package com.crux.qxm.di.userProfileFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.UserProfileFragment;

import dagger.Module;

@Module
public class UserProfileFragmentModule {
    private final UserProfileFragment userProfileFragment;

    public UserProfileFragmentModule(UserProfileFragment userProfileFragment) {
        this.userProfileFragment = userProfileFragment;
    }
}

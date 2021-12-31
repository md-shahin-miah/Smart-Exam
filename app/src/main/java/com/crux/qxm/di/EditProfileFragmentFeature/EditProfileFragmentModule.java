package com.crux.qxm.di.EditProfileFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.EditUserProfileFragment;

import dagger.Module;

@Module
public class EditProfileFragmentModule {
    private final EditUserProfileFragment editUserProfileFragment;

    public EditProfileFragmentModule(EditUserProfileFragment editUserProfileFragment) {
        this.editUserProfileFragment = editUserProfileFragment;
    }
}

package com.crux.qxm.di.profilePicUploadFragmentFeature;

import com.crux.qxm.views.fragments.loginActivityFragments.ProfilePicUploadFragment;

import dagger.Module;

@Module
public class ProfilePicUploadFragmentModule {
    private final ProfilePicUploadFragment profilePicUploadFragment;

    public ProfilePicUploadFragmentModule(ProfilePicUploadFragment profilePicUploadFragment) {
        this.profilePicUploadFragment = profilePicUploadFragment;
    }
}

package com.crux.qxm.di.publicGroupFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.PublicGroupFragment;

import dagger.Module;

@Module
public class PublicGroupFragmentModule {
    private final PublicGroupFragment publicGroupFragment;

    public PublicGroupFragmentModule(PublicGroupFragment publicGroupFragment) {
        this.publicGroupFragment = publicGroupFragment;
    }
}

package com.crux.qxm.di.participateSingleMCQFragmentFeature;

import com.crux.qxm.views.fragments.singleMCQ.ParticipateSingleMCQFragment;

import dagger.Module;

@Module
public class ParticipateSingleMCQFragmentModule {
    private final ParticipateSingleMCQFragment participateSingleMCQFragment;

    public ParticipateSingleMCQFragmentModule(ParticipateSingleMCQFragment participateSingleMCQFragment) {
        this.participateSingleMCQFragment = participateSingleMCQFragment;
    }
}

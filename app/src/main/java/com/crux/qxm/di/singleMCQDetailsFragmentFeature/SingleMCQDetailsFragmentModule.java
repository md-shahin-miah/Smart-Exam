package com.crux.qxm.di.singleMCQDetailsFragmentFeature;

import com.crux.qxm.views.fragments.singleMCQ.SingleMCQDetailsFragment;

import dagger.Module;

@Module
public class SingleMCQDetailsFragmentModule {
    private final SingleMCQDetailsFragment singleMCQDetailsFragment;

    public SingleMCQDetailsFragmentModule(SingleMCQDetailsFragment singleMCQDetailsFragment) {
        this.singleMCQDetailsFragment = singleMCQDetailsFragment;
    }
}

package com.crux.qxm.di.singleMCQOverviewFragmentFeature;

import com.crux.qxm.views.fragments.singleMCQ.SingleMCQOverviewFragment;

import dagger.Module;

@Module
public class SingleMCQOverviewFragmentModule {
    private final SingleMCQOverviewFragment singleMCQOverviewFragment;

    public SingleMCQOverviewFragmentModule(SingleMCQOverviewFragment singleMCQOverviewFragment) {
        this.singleMCQOverviewFragment = singleMCQOverviewFragment;
    }
}

package com.crux.qxm.di.pollOverviewFragmentFeature;

import com.crux.qxm.views.fragments.poll.PollOverviewFragment;

import dagger.Module;

@Module
public class PollOverviewFragmentModule {
    private final PollOverviewFragment pollOverviewFragment;

    public PollOverviewFragmentModule(PollOverviewFragment pollOverviewFragment) {
        this.pollOverviewFragment = pollOverviewFragment;
    }
}

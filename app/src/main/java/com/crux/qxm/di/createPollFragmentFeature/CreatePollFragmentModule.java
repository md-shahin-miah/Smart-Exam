package com.crux.qxm.di.createPollFragmentFeature;

import com.crux.qxm.views.fragments.poll.CreatePollFragment;

import dagger.Module;

@Module
public class CreatePollFragmentModule {
    private final CreatePollFragment createPollFragment;
    public CreatePollFragmentModule(CreatePollFragment createPollFragment) {
        this.createPollFragment = createPollFragment;
    }
}

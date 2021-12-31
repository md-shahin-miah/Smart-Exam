package com.crux.qxm.di.participateQxmFragmentFeature;

import com.crux.qxm.views.fragments.ParticipateQxmFragment;

import dagger.Module;

@Module
public class ParticipateQxmFragmentModule {
    private final ParticipateQxmFragment participateQxmFragment;

    public ParticipateQxmFragmentModule(ParticipateQxmFragment participateQxmFragment) {
        this.participateQxmFragment = participateQxmFragment;
    }
}

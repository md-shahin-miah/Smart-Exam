package com.crux.qxm.di.participationParentFragmentFeature;

import com.crux.qxm.views.fragments.enroll.ParticipationParentFragment;

import dagger.Module;

@Module
public class ParticipationParentFragmentModule {
    private final ParticipationParentFragment participationParentFragment;

    public ParticipationParentFragmentModule(ParticipationParentFragment participationParentFragment) {
        this.participationParentFragment = participationParentFragment;
    }
}

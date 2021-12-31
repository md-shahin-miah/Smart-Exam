package com.crux.qxm.di.enrollParticipatedFragmentFeature;

import com.crux.qxm.views.fragments.enroll.EnrollParticipatedFragment;

import dagger.Module;

@Module
public class EnrollParticipatedFragmentModule {

    private final EnrollParticipatedFragment enrollParticipatedFragment;

    public EnrollParticipatedFragmentModule(EnrollParticipatedFragment enrollParticipatedFragment) {
        this.enrollParticipatedFragment = enrollParticipatedFragment;
    }
}

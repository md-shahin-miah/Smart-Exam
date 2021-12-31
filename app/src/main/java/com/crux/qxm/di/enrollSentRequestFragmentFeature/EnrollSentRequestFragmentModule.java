package com.crux.qxm.di.enrollSentRequestFragmentFeature;

import com.crux.qxm.views.fragments.enroll.EnrollSentRequestFragment;

import dagger.Module;

@Module
public class EnrollSentRequestFragmentModule {
    private final EnrollSentRequestFragment enrollSentRequestFragment;

    public EnrollSentRequestFragmentModule(EnrollSentRequestFragment enrollSentRequestFragment) {
        this.enrollSentRequestFragment = enrollSentRequestFragment;
    }
}

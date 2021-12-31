package com.crux.qxm.di.enrollReceivedRequestFragmentFeature;

import com.crux.qxm.views.fragments.enroll.EnrollReceivedRequestFragment;

import dagger.Module;

@Module
public class EnrollReceivedRequestFragmentModule {
    private final EnrollReceivedRequestFragment enrollReceivedRequestFragment;

    public EnrollReceivedRequestFragmentModule(EnrollReceivedRequestFragment enrollReceivedRequestFragment) {
        this.enrollReceivedRequestFragment = enrollReceivedRequestFragment;
    }
}

package com.crux.qxm.di.enrollYouEnrolledFragmentFeature;

import com.crux.qxm.views.fragments.enroll.EnrollYouEnrolledFragment;

import dagger.Module;

@Module
public class EnrollYouEnrolledFragmentModule {

    private final EnrollYouEnrolledFragment enrollYouEnrolledFragment;

    public EnrollYouEnrolledFragmentModule(EnrollYouEnrolledFragment enrollYouEnrolledFragment) {
        this.enrollYouEnrolledFragment = enrollYouEnrolledFragment;
    }
}

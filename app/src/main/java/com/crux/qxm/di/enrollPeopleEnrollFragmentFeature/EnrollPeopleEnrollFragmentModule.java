package com.crux.qxm.di.enrollPeopleEnrollFragmentFeature;

import com.crux.qxm.views.fragments.enroll.EnrollPeopleEnrollFragment;

import dagger.Module;

@Module
public class EnrollPeopleEnrollFragmentModule {
    private final EnrollPeopleEnrollFragment enrollPeopleEnrollFragment;

    public EnrollPeopleEnrollFragmentModule(EnrollPeopleEnrollFragment enrollPeopleEnrollFragment) {
        this.enrollPeopleEnrollFragment = enrollPeopleEnrollFragment;
    }
}

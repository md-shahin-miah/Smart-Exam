package com.crux.qxm.di.enrollYouEnrolledFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.enroll.EnrollYouEnrolledFragment;

import dagger.Component;

@EnrollYouEnrolledFragmentScope
@Component (modules = EnrollYouEnrolledFragmentModule.class, dependencies = AppComponent.class)
public interface EnrollYouEnrolledFragmentComponent {
    void injectEnrollYouEnrolledFragmentFeature(EnrollYouEnrolledFragment enrollYouEnrolledFragment);
}

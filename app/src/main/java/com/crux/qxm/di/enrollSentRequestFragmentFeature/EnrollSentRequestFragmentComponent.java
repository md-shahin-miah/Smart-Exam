package com.crux.qxm.di.enrollSentRequestFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.enroll.EnrollSentRequestFragment;

import dagger.Component;

@EnrollSentRequestFragmentScope
@Component (modules = EnrollSentRequestFragmentModule.class, dependencies = AppComponent.class)
public interface EnrollSentRequestFragmentComponent {
    void injectEnrollSentRequestFragmentFeature(EnrollSentRequestFragment enrollSentRequestFragment);
}

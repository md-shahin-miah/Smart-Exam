package com.crux.qxm.di.enrollReceivedRequestFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.enroll.EnrollReceivedRequestFragment;

import dagger.Component;

@EnrollReceivedRequestFragmentScope
@Component (modules = EnrollReceivedRequestFragmentModule.class, dependencies = AppComponent.class)
public interface EnrollReceivedRequestFragmentComponent {

    void injectEnrollReceivedRequestFragmentFeature(EnrollReceivedRequestFragment enrollReceivedRequestFragment);

}

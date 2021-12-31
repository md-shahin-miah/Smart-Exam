package com.crux.qxm.di.enrollPeopleEnrollFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.enroll.EnrollPeopleEnrollFragment;

import dagger.Component;

@EnrollPeopleEnrollFragmentScope
@Component (modules = EnrollPeopleEnrollFragmentModule.class, dependencies = AppComponent.class)
public interface EnrollPeopleEnrollFragmentComponent {

    void injectEnrollPeopleEnrollFragment (EnrollPeopleEnrollFragment enrollPeopleEnrollFragment);

}

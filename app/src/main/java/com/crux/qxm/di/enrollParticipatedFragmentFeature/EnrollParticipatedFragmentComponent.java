package com.crux.qxm.di.enrollParticipatedFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.enroll.EnrollParticipatedFragment;

import dagger.Component;

@EnrollParticipatedFragmentScope
@Component (modules = EnrollParticipatedFragmentModule.class, dependencies = AppComponent.class)
public interface EnrollParticipatedFragmentComponent {

    void injectEnrollParticipatedFragment (EnrollParticipatedFragment enrollParticipatedFragment);

}

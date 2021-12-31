package com.crux.qxm.di.participationParentFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.enroll.ParticipationParentFragment;

import dagger.Component;

@ParticipationParentFragmentScope
@Component (modules = ParticipationParentFragmentModule.class, dependencies = AppComponent.class)
public interface ParticipationParentFragmentComponent {
    void injectParticipationParentFragmentFeature(ParticipationParentFragment participationParentFragment);
}

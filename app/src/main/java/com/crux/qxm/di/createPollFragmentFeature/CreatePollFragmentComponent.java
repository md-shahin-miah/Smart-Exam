package com.crux.qxm.di.createPollFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.poll.CreatePollFragment;

import dagger.Component;

@CreatePollFragmentScope
@Component (modules = CreatePollFragmentModule.class, dependencies = AppComponent.class)
public interface CreatePollFragmentComponent {
    void injectCreatePollFragmentFeature(CreatePollFragment createPollFragment);
}

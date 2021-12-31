package com.crux.qxm.di.singleMCQDetailsFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.SingleMCQDetailsFragment;

import dagger.Component;

@SingleMCQDetailsFragmentScope
@Component (modules = SingleMCQDetailsFragmentModule.class, dependencies = AppComponent.class)
public interface SingleMCQDetailsFragmentComponent {

    void injectSingleMCQDetailsFragmentFeature(SingleMCQDetailsFragment singleMCQDetailsFragment);
}

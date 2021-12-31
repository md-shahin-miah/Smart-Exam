package com.crux.qxm.di.fullResultSingleMCQFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.FullResultSingleMCQFragment;

import dagger.Component;
@FullResultSingleMCQFragmentScope
@Component (modules = FullResultSingleMCQFragmentModule.class, dependencies = AppComponent.class)
public interface FullResultSingleMCQFragmentComponent {

    void injectFullResultSingleMCQFragmentFeature(FullResultSingleMCQFragment fullResultSingleMCQFragment);
}

package com.crux.qxm.di.fullResultFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.qxmResult.FullResultFragment;

import dagger.Component;

@FullResultFragmentScope
@Component (modules = FullResultFragmentModule.class, dependencies = AppComponent.class)
public interface FullResultFragmentComponent {

    void injectFullResultFragment (FullResultFragment FullResultFragment);
}

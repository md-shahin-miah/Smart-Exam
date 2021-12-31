package com.crux.qxm.di.singleMCQOverviewFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.SingleMCQOverviewFragment;

import dagger.Component;

@SingleMCQOverviewFragmentScope
@Component (modules = SingleMCQOverviewFragmentModule.class, dependencies = AppComponent.class)
public interface SingleMCQOverviewFragmentComponent {
    void injectSingleMCQOverviewFragmentFeature(SingleMCQOverviewFragment singleMCQOverviewFragment);
}

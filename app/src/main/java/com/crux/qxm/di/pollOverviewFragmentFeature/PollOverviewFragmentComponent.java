package com.crux.qxm.di.pollOverviewFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.poll.PollOverviewFragment;

import dagger.Component;

@PollOverviewFragmentScope
@Component (modules = PollOverviewFragmentModule.class, dependencies = AppComponent.class)
public interface PollOverviewFragmentComponent {
    void injectPollOverviewFragmentFeature(PollOverviewFragment pollOverviewFragment);
}

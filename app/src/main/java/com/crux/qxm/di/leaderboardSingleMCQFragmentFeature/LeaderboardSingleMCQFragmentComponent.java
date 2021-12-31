package com.crux.qxm.di.leaderboardSingleMCQFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.LeaderboardSingleMCQFragment;

import dagger.Component;

@LeaderboardSingleMCQFragmentScope
@Component (modules = LeaderboardSingleMCQFragmentModule.class, dependencies = AppComponent.class)
public interface LeaderboardSingleMCQFragmentComponent {

    void injectLeaderboardSingleMCQFragmentFeature(LeaderboardSingleMCQFragment leaderboardSingleMCQFragment);

}


package com.crux.qxm.di.leaderboardFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.leaderboard.LeaderboardFragment;

import dagger.Component;

@LeaderboardFragmentScope
@Component (modules = LeaderboardFragmentModule.class, dependencies = AppComponent.class)
public interface LeaderboardFragmentComponent {
    void injectLeaderboardFragmentFeature(LeaderboardFragment leaderboardFragment);
}

package com.crux.qxm.di.leaderboardFragmentFeature;

import com.crux.qxm.views.fragments.leaderboard.LeaderboardFragment;

import dagger.Module;

@Module
public class LeaderboardFragmentModule {
    final LeaderboardFragment leaderboardFragment;

    public LeaderboardFragmentModule(LeaderboardFragment leaderboardFragment) {
        this.leaderboardFragment = leaderboardFragment;
    }
}

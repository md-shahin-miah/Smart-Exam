package com.crux.qxm.di.leaderboardSingleMCQFragmentFeature;

import com.crux.qxm.views.fragments.singleMCQ.LeaderboardSingleMCQFragment;

import dagger.Module;

@Module
public class LeaderboardSingleMCQFragmentModule {

    private final LeaderboardSingleMCQFragment leaderboardSingleMCQFragment;

    public LeaderboardSingleMCQFragmentModule(LeaderboardSingleMCQFragment leaderboardSingleMCQFragment) {
        this.leaderboardSingleMCQFragment = leaderboardSingleMCQFragment;
    }
}

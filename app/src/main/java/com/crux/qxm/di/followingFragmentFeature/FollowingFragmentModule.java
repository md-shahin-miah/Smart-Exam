package com.crux.qxm.di.followingFragmentFeature;

import com.crux.qxm.views.fragments.following.FollowingFragment;

import dagger.Module;

@Module
public class FollowingFragmentModule {
    private final FollowingFragment followingFragment;

    public FollowingFragmentModule(FollowingFragment followingFragment) {
        this.followingFragment = followingFragment;
    }
}

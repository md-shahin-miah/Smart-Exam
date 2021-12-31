package com.crux.qxm.di.followerListFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.FollowerListFragment;

import dagger.Module;

@Module
public class FollowerListFragmentModule {
    private final FollowerListFragment followerListFragment;

    public FollowerListFragmentModule(FollowerListFragment followerListFragment) {
        this.followerListFragment = followerListFragment;
    }
}

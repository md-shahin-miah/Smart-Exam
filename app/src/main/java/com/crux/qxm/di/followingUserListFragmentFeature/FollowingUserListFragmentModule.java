package com.crux.qxm.di.followingUserListFragmentFeature;

import com.crux.qxm.views.fragments.following.FollowingListFragment;

import dagger.Module;

@Module
public class FollowingUserListFragmentModule {
    private final FollowingListFragment followingListFragment;

    public FollowingUserListFragmentModule(FollowingListFragment followingListFragment) {
        this.followingListFragment = followingListFragment;
    }
}

package com.crux.qxm.di.myFollowingListFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.MyFollowingListFragment;

import dagger.Module;

@Module
public class MyFollowingListFragmentModule {
    private final MyFollowingListFragment myFollowingListFragment;

    public MyFollowingListFragmentModule(MyFollowingListFragment myFollowingListFragment) {
        this.myFollowingListFragment = myFollowingListFragment;
    }
}

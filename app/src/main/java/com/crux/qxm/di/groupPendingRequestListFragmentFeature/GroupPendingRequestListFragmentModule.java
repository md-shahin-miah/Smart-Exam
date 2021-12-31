package com.crux.qxm.di.groupPendingRequestListFragmentFeature;

import com.crux.qxm.views.fragments.group.GroupPendingRequestListFragment;

import dagger.Module;

@Module
public class GroupPendingRequestListFragmentModule {
    private final GroupPendingRequestListFragment groupPendingRequestListFragment;

    public GroupPendingRequestListFragmentModule(GroupPendingRequestListFragment groupPendingRequestListFragment) {
        this.groupPendingRequestListFragment = groupPendingRequestListFragment;
    }
}

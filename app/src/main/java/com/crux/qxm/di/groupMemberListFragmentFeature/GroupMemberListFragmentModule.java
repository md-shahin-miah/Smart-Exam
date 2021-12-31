package com.crux.qxm.di.groupMemberListFragmentFeature;

import com.crux.qxm.views.fragments.group.GroupMemberListFragment;

import dagger.Module;

@Module
public class GroupMemberListFragmentModule {
    private final GroupMemberListFragment groupMemberListFragment;

    public GroupMemberListFragmentModule(GroupMemberListFragment groupMemberListFragment) {
        this.groupMemberListFragment = groupMemberListFragment;
    }
}

package com.crux.qxm.di.groupReceivedJoinRequestFragmentFeature;

import com.crux.qxm.views.fragments.group.GroupReceivedJoinRequestFragment;

import dagger.Module;

@Module
public class GroupReceivedJoinRequestFragmentModule {
    private final GroupReceivedJoinRequestFragment groupReceivedJoinRequestFragment;

    public GroupReceivedJoinRequestFragmentModule(GroupReceivedJoinRequestFragment groupReceivedJoinRequestFragment) {
        this.groupReceivedJoinRequestFragment = groupReceivedJoinRequestFragment;
    }
}

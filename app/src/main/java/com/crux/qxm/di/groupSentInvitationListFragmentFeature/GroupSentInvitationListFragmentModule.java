package com.crux.qxm.di.groupSentInvitationListFragmentFeature;

import com.crux.qxm.views.fragments.group.GroupSentInvitationListFragment;

import dagger.Module;

@Module
public class GroupSentInvitationListFragmentModule {
    private final GroupSentInvitationListFragment groupSentInvitationListFragment;

    public GroupSentInvitationListFragmentModule(GroupSentInvitationListFragment groupSentInvitationListFragment) {
        this.groupSentInvitationListFragment = groupSentInvitationListFragment;
    }
}

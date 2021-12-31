package com.crux.qxm.di.groupJoinRequestSentFragmentFeature;

import com.crux.qxm.views.fragments.group.InviteMembersToGroupFragment;

import dagger.Module;

@Module
public class GroupJoinRequestSentFragmentModule {

    private final InviteMembersToGroupFragment inviteMembersToGroupFragment;

    public GroupJoinRequestSentFragmentModule(InviteMembersToGroupFragment inviteMembersToGroupFragment) {
        this.inviteMembersToGroupFragment = inviteMembersToGroupFragment;
    }
}

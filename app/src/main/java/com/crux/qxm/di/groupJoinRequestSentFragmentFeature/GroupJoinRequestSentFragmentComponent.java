package com.crux.qxm.di.groupJoinRequestSentFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.InviteMembersToGroupFragment;

import dagger.Component;

@GroupJoinRequestSentFragmentScope
@Component (modules = GroupJoinRequestSentFragmentModule.class, dependencies = AppComponent.class)
public interface GroupJoinRequestSentFragmentComponent {

    void injectGroupJoinRequestSentFragmentFeature(InviteMembersToGroupFragment inviteMembersToGroupFragment);
}

package com.crux.qxm.di.groupSentInvitationListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.GroupSentInvitationListFragment;

import dagger.Component;

@GroupSentInvitationListFragmentScope
@Component (modules = GroupSentInvitationListFragmentModule.class, dependencies = AppComponent.class)
public interface GroupSentInvitationListFragmentComponent {

    void injectGroupSentInvitationListFragmentFeature(GroupSentInvitationListFragment groupSentInvitationListFragment);

}

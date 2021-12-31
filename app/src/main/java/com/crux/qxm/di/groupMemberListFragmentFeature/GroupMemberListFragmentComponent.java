package com.crux.qxm.di.groupMemberListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.GroupMemberListFragment;

import dagger.Component;

@GroupMemberListFragmentScope
@Component (modules = GroupMemberListFragmentModule.class, dependencies = AppComponent.class)
public interface GroupMemberListFragmentComponent {

    void injectGroupMemberListFragmentFeature(GroupMemberListFragment groupMemberListFragment);
}

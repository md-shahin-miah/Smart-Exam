package com.crux.qxm.di.groupReceivedJoinRequestFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.GroupReceivedJoinRequestFragment;

import dagger.Component;

@GroupReceivedJoinRequestFragmentScope
@Component (modules = GroupReceivedJoinRequestFragmentModule.class, dependencies = AppComponent.class)
public interface GroupReceivedJoinRequestFragmentComponent {
    void injectGroupReceivedJoinRequestFragmentFeature(GroupReceivedJoinRequestFragment groupReceivedJoinRequestFragment);
}

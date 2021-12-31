package com.crux.qxm.di.groupPendingRequestListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.GroupPendingRequestListFragment;

import dagger.Component;
@GroupPendingRequestListFragmentScope
@Component (modules= GroupPendingRequestListFragmentModule.class, dependencies = AppComponent.class)
public interface GroupPendingRequestListFragmentComponent {

    void injectGroupPendingRequestListFragmentFeature(GroupPendingRequestListFragment groupPendingRequestListFragment);

}

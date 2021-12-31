package com.crux.qxm.di.followerListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.FollowerListFragment;

import dagger.Component;

@FollowerListFragmentScope
@Component(modules = FollowerListFragmentModule.class, dependencies = AppComponent.class)
public interface FollowerListFragmentComponent {
    void injectFollowerListFragmentFeature(FollowerListFragment followerListFragment);
}

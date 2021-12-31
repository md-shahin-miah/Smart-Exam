package com.crux.qxm.di.followingUserListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.following.FollowingListFragment;

import dagger.Component;
@FollowingUserListFragmentScope
@Component (modules = FollowingUserListFragmentModule.class, dependencies = AppComponent.class)
public interface FollowingUserListFragmentComponent {
    void injectFollowingUserListFragmentFeature(FollowingListFragment followingListFragment);
}

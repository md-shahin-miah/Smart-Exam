package com.crux.qxm.di.followingFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.following.FollowingFragment;

import dagger.Component;

@FollowingFragmentScope
@Component(modules = FollowingFragmentModule.class, dependencies = AppComponent.class)
public interface FollowingFragmentComponent {
    void injectFollowingFragmentFeature(FollowingFragment followingFragment);
}

package com.crux.qxm.di.myFollowingListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.MyFollowingListFragment;

import dagger.Component;

@MyFollowingListFragmentScope
@Component(modules = MyFollowingListFragmentModule.class, dependencies = AppComponent.class)
public interface MyFollowingListFragmentComponent {
    void injectMyFollowingListFeature(MyFollowingListFragment myFollowingListFragment);
}

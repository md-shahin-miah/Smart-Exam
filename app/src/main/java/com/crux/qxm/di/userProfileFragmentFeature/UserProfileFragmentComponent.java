package com.crux.qxm.di.userProfileFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.UserProfileFragment;

import dagger.Component;

@UserProfileFragmentScope
@Component (modules = UserProfileFragmentModule.class, dependencies = AppComponent.class)
public interface UserProfileFragmentComponent {
    void injectUserProfileFragmentFeature(UserProfileFragment userProfileFragment);
}

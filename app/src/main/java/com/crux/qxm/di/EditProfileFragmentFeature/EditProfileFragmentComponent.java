package com.crux.qxm.di.EditProfileFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.EditUserProfileFragment;

import dagger.Component;

@EditProfileFragmentScope
@Component (modules = EditProfileFragmentModule.class, dependencies = AppComponent.class)
public interface EditProfileFragmentComponent {
    void injectEditProfileFragmentFeature(EditUserProfileFragment editUserProfileFragment);
}

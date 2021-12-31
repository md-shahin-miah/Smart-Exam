package com.crux.qxm.di.profileSettingsFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.ProfileSettingsFragment;

import dagger.Component;

@ProfileSettingsFragmentScope
@Component (modules = ProfileSettingsFragmentModule.class, dependencies = AppComponent.class)
public interface ProfileSettingsFragmentComponent {
    void injectProfileSettingsFragmentFeature(ProfileSettingsFragment profileSettingsFragment);
}

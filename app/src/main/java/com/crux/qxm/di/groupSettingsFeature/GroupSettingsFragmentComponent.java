package com.crux.qxm.di.groupSettingsFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.GroupSettingsFragment;

import dagger.Component;

@GroupSettingsFragmentScope
@Component (modules = GroupSettingsFragmentModule.class, dependencies = AppComponent.class)
public interface GroupSettingsFragmentComponent {

    void injectGroupSettingsFragmentFeature(GroupSettingsFragment groupSettingsFragment);
}

package com.crux.qxm.di.loggedInDevicesFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.LoggedInDevicesFragment;

import dagger.Component;

@LoggedInDevicesFragmentScope
@Component (modules = LoggedInDevicesFragmentModule.class, dependencies = AppComponent.class)
public interface LoggedInDevicesFragmentComponent {
    void injectLoggedInDevicesFragmentFeature(LoggedInDevicesFragment loggedInDevicesFragment);
}

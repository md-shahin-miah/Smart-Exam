package com.crux.qxm.di.loggedInDevicesFragmentFeature;

import com.crux.qxm.views.fragments.userProfileActivityFragments.LoggedInDevicesFragment;

import dagger.Module;

@Module
public class LoggedInDevicesFragmentModule {
    private final LoggedInDevicesFragment loggedInDevicesFragment;

    public LoggedInDevicesFragmentModule(LoggedInDevicesFragment loggedInDevicesFragment) {
        this.loggedInDevicesFragment = loggedInDevicesFragment;
    }
}

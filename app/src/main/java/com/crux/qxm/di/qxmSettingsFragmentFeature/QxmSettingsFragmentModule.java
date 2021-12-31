package com.crux.qxm.di.qxmSettingsFragmentFeature;

import com.crux.qxm.views.fragments.createQxmActivityFragments.QxmSettingsFragment;

import dagger.Module;

@Module
public class QxmSettingsFragmentModule {
    private final QxmSettingsFragment qxmSettingsFragment;

    public QxmSettingsFragmentModule(QxmSettingsFragment qxmSettingsFragment) {
        this.qxmSettingsFragment = qxmSettingsFragment;
    }
}

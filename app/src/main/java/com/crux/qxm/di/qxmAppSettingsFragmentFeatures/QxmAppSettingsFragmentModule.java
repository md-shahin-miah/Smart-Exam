package com.crux.qxm.di.qxmAppSettingsFragmentFeatures;

import com.crux.qxm.views.fragments.settings.QxmAppSettingsFragment;

import dagger.Module;

@Module
public class QxmAppSettingsFragmentModule {
    private final QxmAppSettingsFragment qxmAppSettingsFragment;

    public QxmAppSettingsFragmentModule(QxmAppSettingsFragment qxmAppSettingsFragment) {
        this.qxmAppSettingsFragment = qxmAppSettingsFragment;
    }
}

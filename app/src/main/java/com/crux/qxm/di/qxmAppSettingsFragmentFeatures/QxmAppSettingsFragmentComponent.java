package com.crux.qxm.di.qxmAppSettingsFragmentFeatures;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.settings.QxmAppSettingsFragment;

import dagger.Component;

@QxmAppSettingsFragmentScope
@Component (modules = QxmAppSettingsFragmentModule.class, dependencies = AppComponent.class)
public interface QxmAppSettingsFragmentComponent {

    void injectQxmAppSettingsFragmentFeature(QxmAppSettingsFragment qxmAppSettingsFragment);
}

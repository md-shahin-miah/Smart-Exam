package com.crux.qxm.di.qxmSettingsFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.createQxmActivityFragments.QxmSettingsFragment;

import dagger.Component;

@QxmSettingsFragmentScope
@Component (modules = QxmSettingsFragmentModule.class, dependencies = AppComponent.class)
public interface QxmSettingsFragmentComponent {
    void injectQxmSettingsFragmentFeature (QxmSettingsFragment qxmSettingsFragment);

}

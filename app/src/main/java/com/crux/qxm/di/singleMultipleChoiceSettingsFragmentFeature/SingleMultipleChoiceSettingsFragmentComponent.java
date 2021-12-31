package com.crux.qxm.di.singleMultipleChoiceSettingsFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.SingleMultipleChoiceSettingsFragment;

import dagger.Component;

@SingleMultipleChoiceSettingsFragmentScope
@Component (modules = SingleMultipleChoiceSettingsFragmentModule.class, dependencies = AppComponent.class)
public interface SingleMultipleChoiceSettingsFragmentComponent {
    void injectSingleMultipleChoiceSettingsFragmentFeature(SingleMultipleChoiceSettingsFragment singleMultipleChoiceSettingsFragment);
}

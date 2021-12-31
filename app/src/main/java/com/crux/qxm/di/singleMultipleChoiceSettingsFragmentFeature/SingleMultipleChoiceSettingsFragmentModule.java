package com.crux.qxm.di.singleMultipleChoiceSettingsFragmentFeature;

import com.crux.qxm.views.fragments.singleMCQ.SingleMultipleChoiceSettingsFragment;

import dagger.Module;

@Module
public class SingleMultipleChoiceSettingsFragmentModule {
    private final SingleMultipleChoiceSettingsFragment singleMultipleChoiceSettingsFragment;

    public SingleMultipleChoiceSettingsFragmentModule(SingleMultipleChoiceSettingsFragment singleMultipleChoiceSettingsFragment) {
        this.singleMultipleChoiceSettingsFragment = singleMultipleChoiceSettingsFragment;
    }
}

package com.crux.qxm.di.createSingleMultipleChoiceFeature;

import com.crux.qxm.views.fragments.singleMCQ.CreateSingleMultipleChoiceFragment;

import dagger.Module;

@Module
public class CreateSingleMultipleChoiceModule {
    private final CreateSingleMultipleChoiceFragment createSingleMultipleChoiceFragment;

    public CreateSingleMultipleChoiceModule(CreateSingleMultipleChoiceFragment createSingleMultipleChoiceFragment) {
        this.createSingleMultipleChoiceFragment = createSingleMultipleChoiceFragment;
    }
}

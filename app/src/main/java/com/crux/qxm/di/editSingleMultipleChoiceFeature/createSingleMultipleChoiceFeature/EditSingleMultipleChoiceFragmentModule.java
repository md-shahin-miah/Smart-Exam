package com.crux.qxm.di.editSingleMultipleChoiceFeature.createSingleMultipleChoiceFeature;

import com.crux.qxm.views.fragments.singleMCQ.EditSingleMultipleChoiceFragment;

import dagger.Module;

@Module
public class EditSingleMultipleChoiceFragmentModule {
    private final EditSingleMultipleChoiceFragment editSingleMultipleChoiceFragment;

    public EditSingleMultipleChoiceFragmentModule(EditSingleMultipleChoiceFragment editSingleMultipleChoiceFragment) {
        this.editSingleMultipleChoiceFragment = editSingleMultipleChoiceFragment;
    }
}

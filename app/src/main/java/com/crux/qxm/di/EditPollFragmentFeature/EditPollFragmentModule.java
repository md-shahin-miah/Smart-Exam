package com.crux.qxm.di.EditPollFragmentFeature;

import com.crux.qxm.views.fragments.poll.EditPollFragment;

import dagger.Module;

@Module
public class EditPollFragmentModule {
    private final EditPollFragment editPollFragment;

    public EditPollFragmentModule(EditPollFragment editPollFragment) {
        this.editPollFragment = editPollFragment;
    }
}

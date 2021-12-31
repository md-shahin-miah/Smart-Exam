package com.crux.qxm.di.editQxmFragmentFeature;

import com.crux.qxm.views.fragments.createQxmActivityFragments.EditQxmFragment;

import dagger.Module;

@Module
public class EditQxmFragmentModule {

    private final EditQxmFragment editQxmFragment;

    public EditQxmFragmentModule(EditQxmFragment editQxmFragment) {
        this.editQxmFragment = editQxmFragment;
    }
}

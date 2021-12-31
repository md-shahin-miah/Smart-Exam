package com.crux.qxm.di.createQxmFragmentFeature;

import com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment;

import dagger.Module;

@Module
public class CreateQxmFragmentModule {

    private final CreateQxmFragment createQxmFragment;

    public CreateQxmFragmentModule(CreateQxmFragment createQxmFragment) {
        this.createQxmFragment = createQxmFragment;
    }
}

package com.crux.qxm.di.createFragmentFeature;

import com.crux.qxm.views.fragments.create.CreateFragment;

import dagger.Module;

@Module
public class CreateFragmentModule {

    private final CreateFragment createFragment;

    public CreateFragmentModule(CreateFragment createFragment) {
        this.createFragment = createFragment;
    }
}

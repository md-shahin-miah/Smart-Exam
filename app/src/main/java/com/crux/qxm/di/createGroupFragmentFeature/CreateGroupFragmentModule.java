package com.crux.qxm.di.createGroupFragmentFeature;

import com.crux.qxm.views.fragments.group.CreateGroupFragment;

import dagger.Module;

@Module
public class CreateGroupFragmentModule {
    private final CreateGroupFragment createGroupFragment;

    public CreateGroupFragmentModule(CreateGroupFragment createGroupFragment) {
        this.createGroupFragment = createGroupFragment;
    }
}

package com.crux.qxm.di.addMembersToGroupFragmentFeature;

import com.crux.qxm.views.fragments.group.AddMembersToGroupFragment;

import dagger.Module;

@Module
public class AddMembersToGroupFragmentModule {
    private final AddMembersToGroupFragment addMembersToGroupFragment;

    public AddMembersToGroupFragmentModule(AddMembersToGroupFragment addMembersToGroupFragment) {
        this.addMembersToGroupFragment = addMembersToGroupFragment;
    }
}

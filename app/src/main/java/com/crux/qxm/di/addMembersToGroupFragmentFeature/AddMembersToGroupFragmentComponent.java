package com.crux.qxm.di.addMembersToGroupFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.AddMembersToGroupFragment;

import dagger.Component;
@AddMembersToGroupFragmentScope
@Component (modules = AddMembersToGroupFragmentModule.class, dependencies = AppComponent.class)
public interface AddMembersToGroupFragmentComponent {
    void injectAddMembersToGroupFragmentFeature(AddMembersToGroupFragment addMembersToGroupFragment);
}

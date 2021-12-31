package com.crux.qxm.di.createGroupFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.CreateGroupFragment;

import dagger.Component;

@CreateGroupFragmentScope
@Component (modules = CreateGroupFragmentModule.class, dependencies = AppComponent.class)
public interface CreateGroupFragmentComponent {

    void injectCreateGroupFragmentFeature(CreateGroupFragment createGroupFragment);

}

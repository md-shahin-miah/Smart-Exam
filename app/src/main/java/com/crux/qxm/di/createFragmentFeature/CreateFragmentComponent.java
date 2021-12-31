package com.crux.qxm.di.createFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.create.CreateFragment;

import dagger.Component;

@CreateFragmentScope
@Component (modules = CreateFragmentModule.class, dependencies = AppComponent.class)
public interface CreateFragmentComponent {

    void injectCreateFragmentFeature(CreateFragment createFragment);
}

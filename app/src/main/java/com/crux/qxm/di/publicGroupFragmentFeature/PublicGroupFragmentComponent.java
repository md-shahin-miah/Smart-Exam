package com.crux.qxm.di.publicGroupFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.userProfileActivityFragments.PublicGroupFragment;

import dagger.Component;

@PublicGroupFragmentScope
@Component (modules = PublicGroupFragmentModule.class, dependencies = AppComponent.class)
public interface PublicGroupFragmentComponent {
    void injectPublicFragmentFeature(PublicGroupFragment publicGroupFragment);
}


package com.crux.qxm.di.homeFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.HomeFragment;

import dagger.Component;

@HomeFragmentScope
@Component (modules = HomeFragmentModule.class,dependencies = AppComponent.class)
public interface HomeFragmentComponent {

    void injectHomeFragmentFeature(HomeFragment homeFragment);
}

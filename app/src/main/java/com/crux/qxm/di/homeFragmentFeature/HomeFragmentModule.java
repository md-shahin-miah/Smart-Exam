package com.crux.qxm.di.homeFragmentFeature;


import com.crux.qxm.views.fragments.HomeFragment;

import dagger.Module;

@Module
public class HomeFragmentModule {

    private final HomeFragment homeFragment;

    public HomeFragmentModule(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }
}

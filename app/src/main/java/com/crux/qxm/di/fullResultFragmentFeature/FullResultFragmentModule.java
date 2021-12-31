package com.crux.qxm.di.fullResultFragmentFeature;

import com.crux.qxm.views.fragments.qxmResult.FullResultFragment;

import dagger.Module;

@Module
public class FullResultFragmentModule {
    private final FullResultFragment fullResultFragment;

    public FullResultFragmentModule(FullResultFragment fullResultFragment) {
        this.fullResultFragment = fullResultFragment;
    }
}

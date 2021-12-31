package com.crux.qxm.di.fullResultSingleMCQFragmentFeature;

import com.crux.qxm.views.fragments.singleMCQ.FullResultSingleMCQFragment;

import dagger.Module;

@Module
public class FullResultSingleMCQFragmentModule {
    private final FullResultSingleMCQFragment fullResultSingleMCQFragment;

    public FullResultSingleMCQFragmentModule(FullResultSingleMCQFragment fullResultSingleMCQFragment) {
        this.fullResultSingleMCQFragment = fullResultSingleMCQFragment;
    }
}

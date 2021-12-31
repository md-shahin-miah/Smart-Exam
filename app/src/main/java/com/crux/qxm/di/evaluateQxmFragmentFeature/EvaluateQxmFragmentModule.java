package com.crux.qxm.di.evaluateQxmFragmentFeature;

import com.crux.qxm.views.fragments.EvaluateQxmFragment;

import dagger.Module;

@Module
public class EvaluateQxmFragmentModule {
    private final EvaluateQxmFragment evaluateQxmFragment;

    public EvaluateQxmFragmentModule(EvaluateQxmFragment evaluateQxmFragment) {
        this.evaluateQxmFragment = evaluateQxmFragment;
    }
}

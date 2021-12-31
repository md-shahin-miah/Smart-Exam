package com.crux.qxm.di.resultSummaryFragmentFeature;

import com.crux.qxm.views.fragments.qxmResult.ResultSummaryFragment;

import dagger.Module;

@Module
public class ResultSummaryFragmentModule {
    private final ResultSummaryFragment resultSummaryFragment;

    public ResultSummaryFragmentModule(ResultSummaryFragment resultSummaryFragment) {
        this.resultSummaryFragment = resultSummaryFragment;
    }
}

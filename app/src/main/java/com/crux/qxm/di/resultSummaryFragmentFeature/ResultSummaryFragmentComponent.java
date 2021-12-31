package com.crux.qxm.di.resultSummaryFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.qxmResult.ResultSummaryFragment;

import dagger.Component;

@ResultSummaryFragmentScope
@Component (modules = ResultSummaryFragmentModule.class, dependencies = AppComponent.class)
public interface ResultSummaryFragmentComponent {
    void injectResultSummaryFragment(ResultSummaryFragment resultSummaryFragment);
}

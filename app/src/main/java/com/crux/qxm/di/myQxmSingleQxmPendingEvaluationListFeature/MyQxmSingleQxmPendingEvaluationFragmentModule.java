package com.crux.qxm.di.myQxmSingleQxmPendingEvaluationListFeature;


import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmPendingEvaluationListFragment;

import dagger.Module;

@Module
public class MyQxmSingleQxmPendingEvaluationFragmentModule {

    private final MyQxmSingleQxmPendingEvaluationListFragment myQxmSingleQxmPendingEvaluationListFragment;

    public MyQxmSingleQxmPendingEvaluationFragmentModule(MyQxmSingleQxmPendingEvaluationListFragment myQxmSingleQxmPendingEvaluationListFragment) {
        this.myQxmSingleQxmPendingEvaluationListFragment = myQxmSingleQxmPendingEvaluationListFragment;
    }
}

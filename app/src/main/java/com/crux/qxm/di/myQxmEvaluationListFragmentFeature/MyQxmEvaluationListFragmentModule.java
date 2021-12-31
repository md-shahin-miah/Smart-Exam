package com.crux.qxm.di.myQxmEvaluationListFragmentFeature;

import com.crux.qxm.views.fragments.evaluationListQxm.MyQxmEvaluationListFragment;

import dagger.Module;

@Module
public class MyQxmEvaluationListFragmentModule {

    private final MyQxmEvaluationListFragment myQxmEvaluationListFragment;

    public MyQxmEvaluationListFragmentModule(MyQxmEvaluationListFragment myQxmEvaluationListFragment) {
        this.myQxmEvaluationListFragment = myQxmEvaluationListFragment;
    }
}

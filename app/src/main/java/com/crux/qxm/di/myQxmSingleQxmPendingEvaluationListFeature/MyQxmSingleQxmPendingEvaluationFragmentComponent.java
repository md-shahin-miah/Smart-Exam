package com.crux.qxm.di.myQxmSingleQxmPendingEvaluationListFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmPendingEvaluationListFragment;

import dagger.Component;

@MyQxmSingleQxmPendingEvaluationFragmentScope
@Component (modules = MyQxmSingleQxmPendingEvaluationFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmSingleQxmPendingEvaluationFragmentComponent {

    void injectMyQxmSingleQxmPendingEvaluationFragmentFeature(MyQxmSingleQxmPendingEvaluationListFragment myQxmSingleQxmPendingEvaluationListFragment);
}

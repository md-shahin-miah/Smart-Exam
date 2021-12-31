package com.crux.qxm.di.myQxmEvaluationListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.evaluationListQxm.MyQxmEvaluationListFragment;

import dagger.Component;

@MyQxmEvaluationListFragmentScope
@Component (modules = MyQxmEvaluationListFragmentModule.class, dependencies = AppComponent.class)
public interface MyQxmEvaluationListFragmentComponent {

    void injectMyQxmEvaluationListFragmentFeature(MyQxmEvaluationListFragment myQxmEvaluationListFragment);

}

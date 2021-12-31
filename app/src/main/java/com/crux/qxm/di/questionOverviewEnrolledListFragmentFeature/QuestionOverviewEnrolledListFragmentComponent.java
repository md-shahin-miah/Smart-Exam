package com.crux.qxm.di.questionOverviewEnrolledListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverviewEnrolledListFragment;

import dagger.Component;

@QuestionOverviewEnrolledListFragmentScope
@Component (modules = QuestionOverviewEnrolledListFragmentModule.class, dependencies = AppComponent.class)
public interface QuestionOverviewEnrolledListFragmentComponent {
    void injectQuestionOverviewEnrolledListFragmentFeature(QuestionOverviewEnrolledListFragment questionOverviewEnrolledListFragment);

}

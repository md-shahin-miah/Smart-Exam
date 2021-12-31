package com.crux.qxm.di.questionOverviewFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverViewFragment;

import dagger.Component;

@QuestionOverviewFragmentScope
@Component (modules = QuestionOverviewFragmentModule.class, dependencies = AppComponent.class)
public interface QuestionOverviewFragmentComponent {
    void injectQuestionOverviewFragmentFeature(QuestionOverViewFragment questionOverViewFragment);
}

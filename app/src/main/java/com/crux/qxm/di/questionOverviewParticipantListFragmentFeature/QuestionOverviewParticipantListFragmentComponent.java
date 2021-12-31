package com.crux.qxm.di.questionOverviewParticipantListFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverviewParticipantListFragment;

import dagger.Component;

@QuestionOverviewParticipantListFragmentScope
@Component (modules = QuestionOverviewParticipantListFragmentModule.class, dependencies = AppComponent.class)
public interface QuestionOverviewParticipantListFragmentComponent {
    void injectQuestionOverviewParticipantListFragmentFeature(QuestionOverviewParticipantListFragment questionOverviewParticipantListFragment);

}

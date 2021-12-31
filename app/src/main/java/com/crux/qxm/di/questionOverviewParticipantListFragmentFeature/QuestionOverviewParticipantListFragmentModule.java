package com.crux.qxm.di.questionOverviewParticipantListFragmentFeature;

import com.crux.qxm.views.fragments.questionOverview.QuestionOverviewParticipantListFragment;

import dagger.Module;

@Module
public class QuestionOverviewParticipantListFragmentModule {
    private final QuestionOverviewParticipantListFragment questionOverviewParticipantListFragment;

    public QuestionOverviewParticipantListFragmentModule(QuestionOverviewParticipantListFragment questionOverviewParticipantListFragment) {
        this.questionOverviewParticipantListFragment = questionOverviewParticipantListFragment;
    }
}

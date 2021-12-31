package com.crux.qxm.di.questionOverviewEnrolledListFragmentFeature;


import com.crux.qxm.views.fragments.questionOverview.QuestionOverviewEnrolledListFragment;

import dagger.Module;

@Module
public class QuestionOverviewEnrolledListFragmentModule {
    private final QuestionOverviewEnrolledListFragment questionOverviewEnrolledListFragment;

    public QuestionOverviewEnrolledListFragmentModule(QuestionOverviewEnrolledListFragment questionOverviewEnrolledListFragment) {
        this.questionOverviewEnrolledListFragment = questionOverviewEnrolledListFragment;
    }
}

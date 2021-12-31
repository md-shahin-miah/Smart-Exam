package com.crux.qxm.di.questionOverviewFragmentFeature;

import com.crux.qxm.views.fragments.questionOverview.QuestionOverViewFragment;

import dagger.Module;

@Module
public class QuestionOverviewFragmentModule {
    private final QuestionOverViewFragment questionOverViewFragment;

    public QuestionOverviewFragmentModule(QuestionOverViewFragment questionOverViewFragment) {
        this.questionOverViewFragment = questionOverViewFragment;
    }
}

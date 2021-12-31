package com.crux.qxm.di.sendFeedbackFragmentFeature;

import com.crux.qxm.views.fragments.sendFeedback.SendFeedbackFragment;

import dagger.Module;

@Module
public class SendFeedbackFragmentModule {

    final SendFeedbackFragment sendFeedbackFragment;

    public SendFeedbackFragmentModule(SendFeedbackFragment sendFeedbackFragment) {
        this.sendFeedbackFragment = sendFeedbackFragment;
    }
}

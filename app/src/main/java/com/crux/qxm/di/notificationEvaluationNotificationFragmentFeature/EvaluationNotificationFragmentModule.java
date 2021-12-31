package com.crux.qxm.di.notificationEvaluationNotificationFragmentFeature;

import com.crux.qxm.views.fragments.notification.EvaluationNotificationFragment;

import dagger.Module;

@Module
public class EvaluationNotificationFragmentModule {
    final EvaluationNotificationFragment evaluationNotificationFragment;

    public EvaluationNotificationFragmentModule(EvaluationNotificationFragment evaluationNotificationFragment) {
        this.evaluationNotificationFragment = evaluationNotificationFragment;
    }
}

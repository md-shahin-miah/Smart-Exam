package com.crux.qxm.di.notificationEvaluationNotificationFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.notification.EvaluationNotificationFragment;

import dagger.Component;

@EvaluationNotificationFragmentScope
@Component (modules = EvaluationNotificationFragmentModule.class, dependencies = AppComponent.class)
public interface EvaluationNotificationFragmentComponent {
    void injectEvaluationNotificationFragmentFeature(EvaluationNotificationFragment evaluationNotificationFragment);
}

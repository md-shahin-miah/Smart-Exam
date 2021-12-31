package com.crux.qxm.di.sendFeedbackFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.sendFeedback.SendFeedbackFragment;

import dagger.Component;

@SendFeedbackFragmentScope
@Component (modules = SendFeedbackFragmentModule.class, dependencies = AppComponent.class)
public interface SendFeedbackFragmentComponent {
    void injectSendFeedbackFragmentFeature(SendFeedbackFragment sendFeedbackFragment);

}

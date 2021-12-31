package com.crux.qxm.di.createSingleMultipleChoiceFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.CreateSingleMultipleChoiceFragment;

import dagger.Component;

@CreateSingleMultipleChoiceScope
@Component (modules = CreateSingleMultipleChoiceModule.class, dependencies = AppComponent.class)
public interface CreateSingleMultipleChoiceComponent {

    void injectCreateSingleMultipleChoiceFeature(CreateSingleMultipleChoiceFragment createSingleMultipleChoiceFragment);
}

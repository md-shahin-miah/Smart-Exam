package com.crux.qxm.di.editSingleMultipleChoiceFeature.createSingleMultipleChoiceFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.EditSingleMultipleChoiceFragment;

import dagger.Component;

@EditSingleMultipleChoiceFragmentScope
@Component(modules = EditSingleMultipleChoiceFragmentModule.class, dependencies = AppComponent.class)
public interface EditSingleMultipleChoiceFragmentComponent {

    void injectEditSingleMultipleChoiceFragmentFeature(EditSingleMultipleChoiceFragment editSingleMultipleChoiceFragment);
}

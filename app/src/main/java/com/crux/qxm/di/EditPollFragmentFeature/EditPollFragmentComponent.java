package com.crux.qxm.di.EditPollFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.poll.EditPollFragment;

import dagger.Component;

@EditPollFragmentScope
@Component (modules = EditPollFragmentModule.class, dependencies = AppComponent.class)
public interface EditPollFragmentComponent {
    void injectEditPollFragmentFeature(EditPollFragment editPollFragment);
}

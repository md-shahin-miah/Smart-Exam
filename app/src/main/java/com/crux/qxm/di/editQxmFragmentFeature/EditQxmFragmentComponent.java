package com.crux.qxm.di.editQxmFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.createQxmActivityFragments.EditQxmFragment;

import dagger.Component;

@EditQxmFragmentScope
@Component (modules = EditQxmFragmentModule.class, dependencies = AppComponent.class)
public interface EditQxmFragmentComponent {

    void injectEditQxmFragmentFeature(EditQxmFragment editQxmFragment);
}

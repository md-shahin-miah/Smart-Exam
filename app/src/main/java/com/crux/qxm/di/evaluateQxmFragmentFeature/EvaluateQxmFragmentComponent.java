package com.crux.qxm.di.evaluateQxmFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.EvaluateQxmFragment;

import dagger.Component;

@EvaluateQxmFragmentScope
@Component(modules = EvaluateQxmFragmentModule.class, dependencies = AppComponent.class)
public interface EvaluateQxmFragmentComponent {
    void injectEvaluateQxmFragmentFeature(EvaluateQxmFragment evaluateQxmFragment);
}

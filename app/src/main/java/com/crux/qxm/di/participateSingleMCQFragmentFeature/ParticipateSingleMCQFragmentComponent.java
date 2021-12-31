package com.crux.qxm.di.participateSingleMCQFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.singleMCQ.ParticipateSingleMCQFragment;

import dagger.Component;

@ParticipateSingleMCQFragmentScope
@Component(modules = ParticipateSingleMCQFragmentModule.class, dependencies = AppComponent.class)
public interface ParticipateSingleMCQFragmentComponent {

    void injectParticipateSingleMCQFragment(ParticipateSingleMCQFragment participateSingleMCQFragment);

}

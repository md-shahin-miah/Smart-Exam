package com.crux.qxm.di.createQxmFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.createQxmActivityFragments.CreateQxmFragment;

import dagger.Component;

@CreateQxmFragmentScope
@Component(modules = CreateQxmFragmentModule.class,dependencies = AppComponent.class)
public interface CreateQxmFragmentComponent {

    void injectCreateQxmFragment(CreateQxmFragment createQxmFragment);

}

package com.crux.qxm.di.myQxmParentFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.MyQxmParentFragment;

import dagger.Component;

@MyQxmParentFragmentScope
@Component (modules = MyQxmParentFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmParentFragmentComponent {

    void injectMyQxmParentFragmentFeature(MyQxmParentFragment myQxmParentFragment);
}

package com.crux.qxm.di.myPollFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.MyQxmMyPollFragment;

import dagger.Component;
@MyPollFragmentScope
@Component (modules = MyPollFragmentModule.class, dependencies = AppComponent.class)
public interface MyPollFragmentComponent {

    void injectMyPollFragmentFeature(MyQxmMyPollFragment myQxmMyPollFragment);
}

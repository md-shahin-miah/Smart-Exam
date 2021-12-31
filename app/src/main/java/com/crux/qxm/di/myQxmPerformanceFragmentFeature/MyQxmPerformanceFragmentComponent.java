package com.crux.qxm.di.myQxmPerformanceFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.MyQxmResultFragment;

import dagger.Component;

@MyQxmPerformanceFragmentScope
@Component (modules = MyQxmPerformanceFragmentModule.class, dependencies = AppComponent.class)
public interface MyQxmPerformanceFragmentComponent {
    void injectMyQxmPerformanceFragmentFeature(MyQxmResultFragment myQxmResultFragment);
}

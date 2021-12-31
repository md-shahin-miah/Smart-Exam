package com.crux.qxm.di.myPerformanceFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.performance.MyPerformance;

import dagger.Component;

@MyPerformanceScope
@Component (modules = MyPerformanceModule.class, dependencies = AppComponent.class)
public interface MyPerformanceComponent {
    void injectMyPerformanceFragmentFeature(MyPerformance myPerformance);
}

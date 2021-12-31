package com.crux.qxm.di.myPerformanceFeature;

import com.crux.qxm.views.fragments.performance.MyPerformance;

import dagger.Module;

@Module
public class MyPerformanceModule {
    final MyPerformance  myPerformance;

    public MyPerformanceModule(MyPerformance myPerformance) {
        this.myPerformance = myPerformance;
    }
}

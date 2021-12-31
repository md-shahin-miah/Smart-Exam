package com.crux.qxm.di.myQxmPerformanceFragmentFeature;

import com.crux.qxm.views.fragments.myQxm.MyQxmResultFragment;

import dagger.Module;

@Module
public class MyQxmPerformanceFragmentModule {
    final MyQxmResultFragment myQxmResultFragment;

    public MyQxmPerformanceFragmentModule(MyQxmResultFragment myQxmResultFragment) {
        this.myQxmResultFragment = myQxmResultFragment;
    }
}

package com.crux.qxm.di.myQxmSingleQxmEnrolledListFeature;


import dagger.Module;

@Module
public class MyQxmSingleEnrolledFragmentModule {

    private final MyQxmSingleEnrolledFragmentModule myQxmSingleEnrolledFragmentModule;

    public MyQxmSingleEnrolledFragmentModule(MyQxmSingleEnrolledFragmentModule myQxmSingleEnrolledFragmentModule) {
        this.myQxmSingleEnrolledFragmentModule = myQxmSingleEnrolledFragmentModule;
    }
}

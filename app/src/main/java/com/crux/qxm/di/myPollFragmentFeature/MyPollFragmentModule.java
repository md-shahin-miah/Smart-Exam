package com.crux.qxm.di.myPollFragmentFeature;

import com.crux.qxm.views.fragments.myQxm.MyQxmMyPollFragment;

import dagger.Module;

@Module
public class MyPollFragmentModule {
    private final MyQxmMyPollFragment myQxmMyPollFragment;

    public MyPollFragmentModule(MyQxmMyPollFragment myQxmMyPollFragment) {
        this.myQxmMyPollFragment = myQxmMyPollFragment;
    }
}

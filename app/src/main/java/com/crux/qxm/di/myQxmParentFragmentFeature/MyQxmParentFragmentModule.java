package com.crux.qxm.di.myQxmParentFragmentFeature;


import com.crux.qxm.views.fragments.myQxm.MyQxmParentFragment;

import dagger.Module;

@Module
public class MyQxmParentFragmentModule {

    private final MyQxmParentFragment myQxmParentFragment;

    public MyQxmParentFragmentModule(MyQxmParentFragment myQxmParentFragment) {
        this.myQxmParentFragment = myQxmParentFragment;
    }
}

package com.crux.qxm.di.myQxmInitialFragmentFeature;


import com.crux.qxm.views.fragments.myQxm.MyQxmInitialFragment;

import dagger.Module;

@Module
public class MyQxmInitialFragmentModule {

    private final MyQxmInitialFragment myQxmInitialFragment;

    public MyQxmInitialFragmentModule(MyQxmInitialFragment myQxmInitialFragment) {
        this.myQxmInitialFragment = myQxmInitialFragment;
    }
}

package com.crux.qxm.di.myQxmGroupFragmentFeature;

import com.crux.qxm.views.fragments.myQxm.MyQxmGroupFragment;

import dagger.Module;

@Module
public class MyQxmGroupFragmentModule {
    final MyQxmGroupFragment myQxmGroupFragment;

    public MyQxmGroupFragmentModule(MyQxmGroupFragment myQxmGroupFragment) {
        this.myQxmGroupFragment = myQxmGroupFragment;
    }
}

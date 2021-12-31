package com.crux.qxm.di.myQxmListFragmentFeature;


import com.crux.qxm.views.fragments.myQxm.list.MyQxmListFragment;

import dagger.Module;

@Module
public class MyQxmListFragmentModule {

    private final MyQxmListFragment myQxmListFragment;

    public MyQxmListFragmentModule(MyQxmListFragment myQxmListFragment) {
        this.myQxmListFragment = myQxmListFragment;
    }
}

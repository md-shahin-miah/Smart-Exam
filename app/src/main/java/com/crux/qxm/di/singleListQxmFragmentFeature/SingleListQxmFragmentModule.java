package com.crux.qxm.di.singleListQxmFragmentFeature;

import com.crux.qxm.views.fragments.myQxm.list.SingleListQxmFragment;

import dagger.Module;

@Module
public class SingleListQxmFragmentModule {
    private final SingleListQxmFragment singleListQxmFragment;

    public SingleListQxmFragmentModule(SingleListQxmFragment singleListQxmFragment) {
        this.singleListQxmFragment = singleListQxmFragment;
    }
}


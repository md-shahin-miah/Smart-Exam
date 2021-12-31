package com.crux.qxm.di.MyQxmSingleQxmDetailsFragmentFeature;


import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmDetailsFragment;

import dagger.Module;

@Module
public class MyQxmSingleQxmDetailsFragmentModule {

    private final MyQxmSingleQxmDetailsFragment myQxmSingleQxmDetailsFragment;

    public MyQxmSingleQxmDetailsFragmentModule(MyQxmSingleQxmDetailsFragment myQxmSingleQxmDetailsFragment) {
        this.myQxmSingleQxmDetailsFragment = myQxmSingleQxmDetailsFragment;
    }
}

package com.crux.qxm.di.myQxmQBankFragmentFeature;

import com.crux.qxm.views.fragments.myQxm.MyQxmQxmDraftFragment;

import dagger.Module;

@Module
public class MyQxmQBankFragmentModule {
    final MyQxmQxmDraftFragment myQxmQxmDraftFragment;

    public MyQxmQBankFragmentModule(MyQxmQxmDraftFragment myQxmQxmDraftFragment) {
        this.myQxmQxmDraftFragment = myQxmQxmDraftFragment;
    }
}

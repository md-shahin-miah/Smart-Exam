package com.crux.qxm.di.viewQxmGroupFragmentFeature;

import com.crux.qxm.views.fragments.group.ViewQxmGroupFragment;

import dagger.Module;

@Module
public class ViewQxmGroupFragmentModule {
    final ViewQxmGroupFragment viewQxmGroupFragment;

    public ViewQxmGroupFragmentModule(ViewQxmGroupFragment viewQxmGroupFragment) {
        this.viewQxmGroupFragment = viewQxmGroupFragment;
    }
}

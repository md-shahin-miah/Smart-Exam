package com.crux.qxm.di.singleListQxmFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.list.SingleListQxmFragment;

import dagger.Component;

@SingleListQxmFragmentScope
@Component (modules = SingleListQxmFragmentModule.class, dependencies = AppComponent.class)
public interface SingleListQxmFragmentComponent {
    void injectSingleListQxmFragmentFeature(SingleListQxmFragment singleListQxmFragment);
}

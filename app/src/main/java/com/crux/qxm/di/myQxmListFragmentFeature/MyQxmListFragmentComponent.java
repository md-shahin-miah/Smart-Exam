package com.crux.qxm.di.myQxmListFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.list.MyQxmListFragment;

import dagger.Component;

@MyQxmListFragmentScope
@Component (modules = MyQxmListFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmListFragmentComponent {

    void injectMyQxmListFragmentFeature(MyQxmListFragment myQxmListFragment);
}

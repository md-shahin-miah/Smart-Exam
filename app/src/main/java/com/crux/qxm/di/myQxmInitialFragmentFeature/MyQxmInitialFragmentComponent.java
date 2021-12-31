package com.crux.qxm.di.myQxmInitialFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.MyQxmInitialFragment;

import dagger.Component;

@MyQxmInitialFragmentScope
@Component (modules = MyQxmInitialFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmInitialFragmentComponent {

    void injectMyQxmInitialFragmentFeature(MyQxmInitialFragment myQxmInitialFragment);
}

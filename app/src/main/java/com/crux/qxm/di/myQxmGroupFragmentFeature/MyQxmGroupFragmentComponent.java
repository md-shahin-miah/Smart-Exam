package com.crux.qxm.di.myQxmGroupFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.MyQxmGroupFragment;

import dagger.Component;

@MyQxmGroupFragmentScope
@Component (modules = MyQxmGroupFragmentModule.class, dependencies = AppComponent.class)
public interface MyQxmGroupFragmentComponent {

    void injectMyQxmGroupFragmentFeature(MyQxmGroupFragment myQxmGroupFragment);
}

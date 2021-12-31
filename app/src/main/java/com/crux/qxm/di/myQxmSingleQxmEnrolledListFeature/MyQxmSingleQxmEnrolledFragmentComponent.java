package com.crux.qxm.di.myQxmSingleQxmEnrolledListFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmEnrolledListFragment;

import dagger.Component;

@MyQxmSingleQxmEnrolledFragmentScope
@Component (modules = MyQxmSingleEnrolledFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmSingleQxmEnrolledFragmentComponent {

    void injectMyQxmSingleQxmEnrolledFragmentFeature(MyQxmSingleQxmEnrolledListFragment myQxmSingleQxmEnrolledListFragment);
}

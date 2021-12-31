package com.crux.qxm.di.myQxmSingleQxmParticipatorListFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmParticipatorListFragment;

import dagger.Component;

@MyQxmSingleQxmParticipatorListFragmentScope
@Component (modules = MyQxmSingleParticipatorListFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmSingleQxmParticipatorListFragmentComponent {

    void injectMyQxmSingleQxmParticipatorListFragmentFeature(MyQxmSingleQxmParticipatorListFragment myQxmSingleQxmParticipatorListFragment);
}

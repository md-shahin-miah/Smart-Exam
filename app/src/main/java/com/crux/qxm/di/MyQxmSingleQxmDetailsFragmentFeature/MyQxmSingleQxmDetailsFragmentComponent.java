package com.crux.qxm.di.MyQxmSingleQxmDetailsFragmentFeature;


import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmDetailsFragment;

import dagger.Component;

@MyQxmSingleQxmDetailsFragmentScope
@Component (modules = MyQxmSingleQxmDetailsFragmentModule.class,dependencies = AppComponent.class)
public interface MyQxmSingleQxmDetailsFragmentComponent {

    void injectMyQxmSingleQxmDetailsFragmentFeature(MyQxmSingleQxmDetailsFragment myQxmSingleQxmDetailsFragment);
}

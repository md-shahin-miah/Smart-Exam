package com.crux.qxm.di.viewQxmGroupFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.group.ViewQxmGroupFragment;

import dagger.Component;

@ViewQxmGroupFragmentScope
@Component (modules = ViewQxmGroupFragmentModule.class, dependencies = AppComponent.class)
public interface ViewQxmGroupFragmentComponent {

    void injectViewQxmGroupFragmentFeature(ViewQxmGroupFragment viewQxmGroupFragment);

}

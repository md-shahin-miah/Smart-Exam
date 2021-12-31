package com.crux.qxm.di.myQxmQBankFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.myQxm.MyQxmQxmDraftFragment;

import dagger.Component;

@MyQxmQBankFragmentScope
@Component (modules = MyQxmQBankFragmentModule.class, dependencies = AppComponent.class)
public interface MyQxmQBankFragmentComponent {
    void injectMyQxmQBankFragmentFeature(MyQxmQxmDraftFragment myQxmQxmDraftFragment);
}

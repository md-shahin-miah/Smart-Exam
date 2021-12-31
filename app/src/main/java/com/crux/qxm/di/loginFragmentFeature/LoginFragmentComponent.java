package com.crux.qxm.di.loginFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.loginActivityFragments.LoginFragment;

import dagger.Component;

@LoginFragmentScope
@Component (modules = LoginFragmentModule.class, dependencies = AppComponent.class)
public interface LoginFragmentComponent {
    void injectLoginFragment(LoginFragment loginFragment);
}

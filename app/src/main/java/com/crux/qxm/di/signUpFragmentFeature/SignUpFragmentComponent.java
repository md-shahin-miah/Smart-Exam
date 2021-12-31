package com.crux.qxm.di.signUpFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.loginActivityFragments.SignUpFragment;

import dagger.Component;

@SignUpFragmentScope
@Component (modules = SignUpFragmentModule.class, dependencies = AppComponent.class)
public interface SignUpFragmentComponent {
    void injectSignUpFragment (SignUpFragment signUpFragment);
}

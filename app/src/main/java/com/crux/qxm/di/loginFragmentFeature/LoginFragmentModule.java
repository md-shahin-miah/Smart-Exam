package com.crux.qxm.di.loginFragmentFeature;

import com.crux.qxm.views.fragments.loginActivityFragments.LoginFragment;

import dagger.Module;

@Module
public class LoginFragmentModule {
    private final LoginFragment loginFragment;

    public LoginFragmentModule(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }
}

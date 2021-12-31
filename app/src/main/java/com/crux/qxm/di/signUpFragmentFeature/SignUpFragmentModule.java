package com.crux.qxm.di.signUpFragmentFeature;

import com.crux.qxm.views.fragments.loginActivityFragments.SignUpFragment;

import dagger.Module;

@Module
public class SignUpFragmentModule {
    private final SignUpFragment signUpFragment;


    public SignUpFragmentModule(SignUpFragment signUpFragment) {
        this.signUpFragment = signUpFragment;
    }
}

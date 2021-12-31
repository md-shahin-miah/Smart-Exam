package com.crux.qxm.di.userInterestAndPreferableLanguageInputFragmentFeature;

import com.crux.qxm.views.fragments.loginActivityFragments.UserInterestAndPreferableLanguageInputFragment;

import dagger.Module;

@Module
public class UserInterestAndPreferableLanguageInputFragmentModule {
    private final UserInterestAndPreferableLanguageInputFragment userInterestAndPreferableLanguageInputFragment;

    public UserInterestAndPreferableLanguageInputFragmentModule(UserInterestAndPreferableLanguageInputFragment userInterestAndPreferableLanguageInputFragment) {
        this.userInterestAndPreferableLanguageInputFragment = userInterestAndPreferableLanguageInputFragment;
    }
}

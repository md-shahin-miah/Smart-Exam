package com.crux.qxm.di.searchFragmentFeature;

import com.crux.qxm.views.fragments.search.SearchableFragment;

import dagger.Module;

@Module
public class SearchFragmentModule {
    private final SearchableFragment searchableFragment;

    public SearchFragmentModule(SearchableFragment searchableFragment) {
        this.searchableFragment = searchableFragment;
    }
}


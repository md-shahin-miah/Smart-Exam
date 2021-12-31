package com.crux.qxm.di.searchFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.search.SearchableFragment;

import dagger.Component;

@SearchableFragmentScope
@Component (modules = SearchFragmentModule.class, dependencies = AppComponent.class)
public interface SearchFragmentComponent {
    void injectSearchFragmentFeature(SearchableFragment searchableFragment);
}

package com.crux.qxm.di.module;


import android.content.Context;

import com.crux.qxm.di.appFeature.scope.ApplicationContext;
import com.crux.qxm.di.appFeature.scope.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @ApplicationContext
    @AppScope
    @Provides
    public Context context(){ return context.getApplicationContext(); }
}

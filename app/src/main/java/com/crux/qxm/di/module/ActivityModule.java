package com.crux.qxm.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.crux.qxm.di.appFeature.scope.AppScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Context context;

    ActivityModule(AppCompatActivity context) {
        this.context = context;
    }

    @Named("activity_context")
    @AppScope
    @Provides
    public Context context(){
        return context;
    }
}

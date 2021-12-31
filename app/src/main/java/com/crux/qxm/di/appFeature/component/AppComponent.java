package com.crux.qxm.di.appFeature.component;


import com.crux.qxm.di.appFeature.module.AppModule;
import com.crux.qxm.di.module.PicassoModule;
import com.crux.qxm.di.appFeature.scope.AppScope;
import com.squareup.picasso.Picasso;

import dagger.Component;
import retrofit2.Retrofit;

@AppScope
@Component(modules = {PicassoModule.class, AppModule.class})
public interface AppComponent {
    Picasso getPicasso();
    Retrofit getRetrofit();
}

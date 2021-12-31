package com.crux.qxm.di.appFeature.module;

import com.crux.qxm.di.appFeature.scope.AppScope;
import com.crux.qxm.di.module.OkHttpClientModule;
import com.crux.qxm.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module (includes = OkHttpClientModule.class)
public class AppModule {

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory (Gson gson){
        return GsonConverterFactory.create(gson);
    }

    @AppScope
    @Provides
    public Retrofit retrofit (OkHttpClient okHttpClient,
                              GsonConverterFactory gsonConverterFactory, Gson gson){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Helper.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();

    }
}

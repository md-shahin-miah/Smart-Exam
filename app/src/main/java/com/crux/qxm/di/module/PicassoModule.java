package com.crux.qxm.di.module;

import android.content.Context;

import com.crux.qxm.di.appFeature.scope.ApplicationContext;
import com.crux.qxm.di.appFeature.scope.AppScope;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = OkHttpClientModule.class)
public class PicassoModule {

    @AppScope
    @Provides
    public Picasso picasso (@ApplicationContext Context context, OkHttp3Downloader okHttp3Downloader){
        return new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build();
    }

    @Provides
    public OkHttp3Downloader okHttp3Downloader (OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

}

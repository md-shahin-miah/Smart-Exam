package com.crux.qxm.di.module;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.crux.qxm.di.appFeature.scope.AppScope;
import com.crux.qxm.di.appFeature.scope.ApplicationContext;
import com.crux.qxm.utils.networkHelper.EnableTLSv12OnPreLollipopHelper;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module(includes = ContextModule.class)
public class OkHttpClientModule {
    private static final String TAG = "OkHttpClientModule";

    @Provides
    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor) {

        /*return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();*/

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                    .addInterceptor(httpLoggingInterceptor);

            return EnableTLSv12OnPreLollipopHelper.enableTls12OnPreLollipop(client).build();

        } else {
            return new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build();
        }
    }

    @Provides
    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000); // 10 MB
    }

    @Provides
    @AppScope
    public File file(@ApplicationContext Context context) {
        File file = new File(context.getCacheDir(), "HttpCache");
        file.mkdirs();
        return file;
    }

    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> {
            Log.d(TAG, message);
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


}

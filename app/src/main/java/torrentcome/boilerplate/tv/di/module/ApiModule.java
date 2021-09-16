package torrentcome.boilerplate.tv.di.module;

import android.content.Context;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import torrentcome.boilerplate.tv.data.remote.ApiService;
import torrentcome.boilerplate.tv.di.AppScope;

@Module
public class ApiModule {

    @AppScope
    @Provides
    public File provideCacheFile(Context context) {
        return context.getFilesDir();
    }

    @AppScope
    @Provides
    public Cache provideCache(File file) {
        return new Cache(file,  10 * 1000);
    }

    @AppScope
    @Provides
    public OkHttpClient provideClient(Cache cache) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request()))
                .addInterceptor(logInterceptor);
        builder.cache(cache);
        return builder.build();
    }

    @AppScope
    @Provides
    public GsonConverterFactory provideConverter() {
        return GsonConverterFactory.create();
    }

    @AppScope
    @Provides
    public RxJavaCallAdapterFactory provideAdapter() {
        return RxJavaCallAdapterFactory.create();
    }

    @AppScope
    @Provides
    public ApiService provideApi(OkHttpClient client, GsonConverterFactory converter, RxJavaCallAdapterFactory adapter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(client)
                .addConverterFactory(converter)
                .addCallAdapterFactory(adapter)
                .build();
        return retrofit.create(ApiService.class);
    }
}
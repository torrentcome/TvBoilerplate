package torrentcome.boilerplate.tv.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public interface WebBoilerplateTvService {
    String ENDPOINT = "https://jsonplaceholder.typicode.com/";

    class Creator {
        public static WebBoilerplateTvService endpointService() {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> chain.proceed(chain.request())).addInterceptor(logInterceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebBoilerplateTvService.ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(WebBoilerplateTvService.class);
        }
    }
}

package torrentcome.boilerplate.tv;

import android.app.Application;

import torrentcome.boilerplate.tv.di.module.ApiModule;
import torrentcome.boilerplate.tv.di.module.ContextModule;
import timber.log.Timber;
import torrentcome.boilerplate.tv.di.module.SharedPrefModule;

public class App extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .apiModule(new ApiModule())
                .sharedPrefModule(new SharedPrefModule())
                .build();
    }
}

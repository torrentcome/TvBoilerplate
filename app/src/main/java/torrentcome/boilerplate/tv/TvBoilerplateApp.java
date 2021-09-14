package torrentcome.boilerplate.tv;

import android.app.Application;
import android.content.Context;

import torrentcome.boilerplate.tv.di.component.ApplicationComponent;
import torrentcome.boilerplate.tv.di.component.DaggerApplicationComponent;
import torrentcome.boilerplate.tv.di.module.ApplicationModule;
import timber.log.Timber;

public class TvBoilerplateApp extends Application {

    private ApplicationComponent mApplicationComponent;

    public static TvBoilerplateApp get(Context context) {
        return (TvBoilerplateApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}

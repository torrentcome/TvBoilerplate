package torrentcome.boilerplate.tv.di.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import torrentcome.boilerplate.tv.di.AppScope;

@Module
public class ContextModule {
    private final Context context;

    public ContextModule(Application app) {
        this.context = app;
    }

    @Provides
    @AppScope
    Context provideContext() {
        return context;
    }
}
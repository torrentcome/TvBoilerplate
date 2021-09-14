package torrentcome.boilerplate.tv.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import torrentcome.boilerplate.tv.data.DataManager;
import torrentcome.boilerplate.tv.data.local.PreferencesHelper;
import torrentcome.boilerplate.tv.di.ApplicationContext;
import torrentcome.boilerplate.tv.di.module.ApplicationModule;
import rx.subscriptions.CompositeSubscription;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    PreferencesHelper preferencesHelper();

    DataManager dataManager();

    CompositeSubscription compositeSubscription();
}
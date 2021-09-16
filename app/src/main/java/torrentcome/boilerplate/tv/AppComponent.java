package torrentcome.boilerplate.tv;

import dagger.Component;
import torrentcome.boilerplate.tv.data.DataManager;
import torrentcome.boilerplate.tv.di.AppScope;
import torrentcome.boilerplate.tv.di.module.ApiModule;
import torrentcome.boilerplate.tv.di.module.ContextModule;
import torrentcome.boilerplate.tv.di.module.SharedPrefModule;

@AppScope
@Component(modules = {
        ContextModule.class,
        ApiModule.class,
        SharedPrefModule.class})
public interface AppComponent {
    DataManager dataManager();
}
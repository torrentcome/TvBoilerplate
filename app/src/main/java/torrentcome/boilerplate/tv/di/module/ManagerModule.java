package torrentcome.boilerplate.tv.di.module;


import dagger.Module;
import dagger.Provides;
import torrentcome.boilerplate.tv.data.DataManager;
import torrentcome.boilerplate.tv.data.remote.ApiService;
import torrentcome.boilerplate.tv.di.AppScope;

@Module
public class ManagerModule {
    @Provides
    @AppScope
    DataManager provideDataManager(ApiService apiService) {
        return new DataManager(apiService);
    }
}
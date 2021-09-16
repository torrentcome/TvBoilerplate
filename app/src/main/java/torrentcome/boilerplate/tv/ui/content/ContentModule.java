package torrentcome.boilerplate.tv.ui.content;

import dagger.Module;
import dagger.Provides;
import torrentcome.boilerplate.tv.data.DataManager;
import torrentcome.boilerplate.tv.di.ActivityScope;

@Module
public class ContentModule {

    @Provides
    @ActivityScope
    ContentPresenter providePresenter(DataManager dataManager) {
        return new ContentPresenter(dataManager);
    }
}
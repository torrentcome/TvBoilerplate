package torrentcome.boilerplate.tv.di.component;

import dagger.Component;
import torrentcome.boilerplate.tv.di.PerActivity;
import torrentcome.boilerplate.tv.di.module.ActivityModule;
import torrentcome.boilerplate.tv.ui.content.ContentFragment;
import torrentcome.boilerplate.tv.ui.search.SearchContentFragment;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ContentFragment contentFragment);

    void inject(SearchContentFragment searchContentFragment);

}
package torrentcome.boilerplate.tv.ui.content;

import dagger.Component;
import torrentcome.boilerplate.tv.di.ActivityScope;
import torrentcome.boilerplate.tv.AppComponent;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ContentModule.class})
interface ContentComponent {
     void inject(ContentFragment contentFragment);
}
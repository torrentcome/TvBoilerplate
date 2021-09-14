package torrentcome.boilerplate.tv.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import torrentcome.boilerplate.tv.data.local.PreferencesHelper;
import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.data.remote.WebBoilerplateTvService;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class DataManager {

    private final WebBoilerplateTvService mTvWebBoilerplateTvService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, WebBoilerplateTvService webBoilerplateTvService) {
        mPreferencesHelper = preferencesHelper;
        mTvWebBoilerplateTvService = webBoilerplateTvService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Single<List<Photo>> get(List<Photo> photos) {
        return Single.just(photos);
    }

}

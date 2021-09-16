package torrentcome.boilerplate.tv.data;

import java.util.List;

import javax.inject.Inject;

import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.data.remote.ApiService;
import io.reactivex.rxjava3.core.Single;

public class DataManager {
    private final ApiService api;

    @Inject
    public DataManager(ApiService apiService) {
        this.api = apiService;
    }

    public Single<List<Photo>> getPhotos() {
        return api.getPhotos();
    }
}
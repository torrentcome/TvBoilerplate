package torrentcome.boilerplate.tv.data.remote;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import torrentcome.boilerplate.tv.domain.Photo;

public interface ApiService {
    @GET("photos/")
    Single<List<Photo>> getPhotos();
}

package torrentcome.boilerplate.tv.ui.content;

import java.util.List;

import javax.inject.Inject;

import torrentcome.boilerplate.tv.data.DataManager;
import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.base.BasePresenter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class ContentPresenter extends BasePresenter<ContentMvpView> {

    private final DataManager mDataManager;

    @Inject
    public ContentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void getDatas(List<Photo> photos) {
        checkViewAttached();

        mDataManager.get(photos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Photo>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            @Override
                            public void onSuccess(@NonNull List<Photo> photos) {
                                getMvpView().show(photos);
                            }

                            @Override
                            public void onError(@NonNull Throwable error) {
                                getMvpView().showError();
                                Timber.e(error, "There was an error loading the data !");
                            }
                        });
    }

}
package torrentcome.boilerplate.tv.ui.search;

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

public class SearchContentPresenter extends BasePresenter<SearchContentBaseView> {

    private final DataManager mDataManager;

    @Inject
    public SearchContentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void search() {
        mDataManager.getPhotos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Photo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Photo> photos) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

}
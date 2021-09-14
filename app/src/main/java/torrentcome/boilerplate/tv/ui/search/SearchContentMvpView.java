package torrentcome.boilerplate.tv.ui.search;

import java.util.List;

import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.base.MvpView;

public interface SearchContentMvpView extends MvpView {

    void show(List<Photo> photos);

    void showError();
}
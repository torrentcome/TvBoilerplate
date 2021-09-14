package torrentcome.boilerplate.tv.ui.content;

import java.util.List;

import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.base.MvpView;

public interface ContentMvpView extends MvpView {

    void show(List<Photo> photos);

    void showError();
}
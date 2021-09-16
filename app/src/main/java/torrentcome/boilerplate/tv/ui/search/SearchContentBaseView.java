package torrentcome.boilerplate.tv.ui.search;

import java.util.List;

import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.base.BaseView;

public interface SearchContentBaseView extends BaseView {

    void show(List<Photo> photos);

    void showError();
}
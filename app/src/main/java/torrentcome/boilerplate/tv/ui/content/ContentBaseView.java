package torrentcome.boilerplate.tv.ui.content;

import java.util.List;

import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.base.BaseView;

public interface ContentBaseView extends BaseView {

    void show(List<Photo> photos);

    void showError();
}
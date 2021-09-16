package torrentcome.boilerplate.tv.ui.base;

public class BasePresenter<T extends BaseView> {
    public T view;

    public void attachView(T view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

}
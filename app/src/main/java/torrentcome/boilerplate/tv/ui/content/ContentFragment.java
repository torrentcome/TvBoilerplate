package torrentcome.boilerplate.tv.ui.content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import torrentcome.boilerplate.tv.R;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import torrentcome.boilerplate.tv.App;
import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.common.CardPresenter;
import torrentcome.boilerplate.tv.ui.search.SearchContentActivity;

public class ContentFragment extends BrowseFragment implements ContentBaseView {

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private final OnItemViewClickedListener mOnItemViewClickedListener = (itemViewHolder, item, rowViewHolder, row) -> {
    };

    @Inject
    ContentPresenter contentPresenter;

    private ArrayObjectAdapter rowsAdapter;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private Drawable mDefaultBackground;
    private Handler handler;
    private Runnable mBackgroundRunnable;
    private final OnItemViewSelectedListener mOnItemViewSelectedListener = (itemViewHolder, item, rowViewHolder, row) -> {
                if (item instanceof Photo) {
                    Photo photo = (Photo) item;
                    String backgroundUrl = photo.url;
                    if (backgroundUrl != null) startBackgroundTimer(URI.create(backgroundUrl));
                }
            };

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerContentComponent.builder().appComponent(App.getAppComponent()).contentModule(new ContentModule()).build().inject(this);

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        handler = new Handler();
        contentPresenter.attachView(this);
        setAdapter(rowsAdapter);

        prepareBackgroundManager();

        setupUIElements();
        setupListeners();

        getDatas();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundRunnable != null) {
            handler.removeCallbacks(mBackgroundRunnable);
            mBackgroundRunnable = null;
        }
        mBackgroundManager = null;
        contentPresenter.detachView();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBackgroundManager.release();
    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBackgroundManager.setBitmap(resource);
                    }
                });
        if (mBackgroundRunnable != null) handler.removeCallbacks(mBackgroundRunnable);
    }

    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_channel_foreground));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.primary));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.accent));
    }

    private void setupListeners() {
        setOnItemViewClickedListener(mOnItemViewClickedListener);
        setOnItemViewSelectedListener(mOnItemViewSelectedListener);
        setOnSearchClickedListener(view -> startActivity(new Intent(getActivity(), SearchContentActivity.class)));
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void getDatas() {
        contentPresenter.getDatas();
    }

    private void startBackgroundTimer(final URI backgroundURI) {
        if (mBackgroundRunnable != null) handler.removeCallbacks(mBackgroundRunnable);
        mBackgroundRunnable = () -> {
            if (backgroundURI != null) updateBackground(backgroundURI.toString());
        };
        handler.postDelayed(mBackgroundRunnable, BACKGROUND_UPDATE_DELAY);
    }

    @Override
    public void show(List<Photo> photos) {
        final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        listRowAdapter.addAll(0, photos);
        HeaderItem header = new HeaderItem(0, getString(R.string.header_title));
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    @Override
    public void showError() {
        String errorMessage = getString(R.string.error_message_generic);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

}
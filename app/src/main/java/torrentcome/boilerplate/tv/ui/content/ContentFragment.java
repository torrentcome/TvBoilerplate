package torrentcome.boilerplate.tv.ui.content;

import android.content.Intent;
import android.content.res.Resources;
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
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import torrentcome.boilerplate.tv.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import torrentcome.boilerplate.tv.domain.Photo;
import torrentcome.boilerplate.tv.ui.base.BaseActivity;
import torrentcome.boilerplate.tv.ui.common.CardPresenter;
import torrentcome.boilerplate.tv.ui.search.SearchContentActivity;

public class ContentFragment extends BrowseFragment implements ContentMvpView {

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private final OnItemViewClickedListener mOnItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            // respond to item clicks
        }
    };

    @Inject
    ContentPresenter mContentPresenter;

    private ArrayObjectAdapter mRowsAdapter;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private Drawable mDefaultBackground;
    private Handler mHandler;
    private Runnable mBackgroundRunnable;
    private final OnItemViewSelectedListener mOnItemViewSelectedListener =
            new OnItemViewSelectedListener() {
                @Override
                public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                    // respond to item selection
                    if (item instanceof Photo) {
                        Photo photo = (Photo) item;
                        String backgroundUrl = photo.url;
                        if (backgroundUrl != null) startBackgroundTimer(URI.create(backgroundUrl));
                    }
                }
            };

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mHandler = new Handler();
        mContentPresenter.attachView(this);
        setAdapter(mRowsAdapter);
        prepareBackgroundManager();
        setupUIElements();
        setupListeners();
        getDatas();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundRunnable != null) {
            mHandler.removeCallbacks(mBackgroundRunnable);
            mBackgroundRunnable = null;
        }
        mBackgroundManager = null;
        mContentPresenter.detachView();
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
        if (mBackgroundRunnable != null) mHandler.removeCallbacks(mBackgroundRunnable);
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
        Resources resources = getResources();
        String[] names = resources.getStringArray(R.array.names);
        String[] descriptions = resources.getStringArray(R.array.descriptions);
        String[] images = resources.getStringArray(R.array.images);

        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            photos.add(new Photo(names[i], descriptions[i], images[i]));
        }
        mContentPresenter.getDatas(photos);
    }

    private void startBackgroundTimer(final URI backgroundURI) {
        if (mBackgroundRunnable != null) mHandler.removeCallbacks(mBackgroundRunnable);
        mBackgroundRunnable = new Runnable() {
            @Override
            public void run() {
                if (backgroundURI != null) updateBackground(backgroundURI.toString());
            }
        };
        mHandler.postDelayed(mBackgroundRunnable, BACKGROUND_UPDATE_DELAY);
    }

    @Override
    public void show(List<Photo> photos) {
        final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        listRowAdapter.addAll(0, photos);
        HeaderItem header = new HeaderItem(0, getString(R.string.header_title));
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    @Override
    public void showError() {
        // show loading error state here
        String errorMessage = getString(R.string.error_message_generic);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

}
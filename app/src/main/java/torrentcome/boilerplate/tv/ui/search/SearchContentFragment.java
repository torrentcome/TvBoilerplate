package torrentcome.boilerplate.tv.ui.search;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import torrentcome.boilerplate.tv.utils.NetworkUtils;
import torrentcome.boilerplate.tv.utils.ToastFactory;
import timber.log.Timber;

public class SearchContentFragment extends SearchFragment implements SearchContentMvpView, SearchFragment.SearchResultProvider {
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int REQUEST_SPEECH = 0x00000010;

    @Inject
    SearchContentPresenter mSearchContentPresenter;

    private ArrayObjectAdapter mResultsAdapter;
    private ArrayObjectAdapter mSearchObjectAdapter;
    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Handler mHandler;
    private Runnable mBackgroundRunnable;

    private String mSearchQuery;
    private final OnItemViewClickedListener mOnItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            // Handle item click
        }
    };
    private final OnItemViewSelectedListener mOnItemViewSelectedListener =
            new OnItemViewSelectedListener() {
                @Override
                public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                    if (item instanceof Photo) {
                        String backgroundUrl = ((Photo) item).url;
                        if (backgroundUrl != null) startBackgroundTimer(URI.create(backgroundUrl));
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        mResultsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mHandler = new Handler();
        mSearchContentPresenter.attachView(this);
        setSearchResultProvider(this);
        setupBackgroundManager();
        setListeners();
    }

    public void onDestroy() {
        if (mBackgroundRunnable != null) {
            mHandler.removeCallbacks(mBackgroundRunnable);
            mBackgroundRunnable = null;
        }
        mBackgroundManager = null;
        mSearchContentPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBackgroundManager.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_SPEECH == requestCode) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    setSearchQuery(data, false);
                    break;
                case Activity.RESULT_CANCELED:
                    Timber.i("Recognizer canceled");
                    break;
            }
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mResultsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        loadQuery(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadQuery(query);
        return true;
    }

    public boolean hasResults() {
        return mResultsAdapter.size() > 0;
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
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap>
                                                        glideAnimation) {
                        mBackgroundManager.setBitmap(resource);
                    }
                });
        if (mBackgroundRunnable != null) mHandler.removeCallbacks(mBackgroundRunnable);
    }

    private void setupBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mDefaultBackground =
                new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
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

    private void setListeners() {
        setOnItemViewClickedListener(mOnItemViewClickedListener);
        setOnItemViewSelectedListener(mOnItemViewSelectedListener);
        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    try {
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException error) {
                        Timber.e(error, "Cannot find activity for speech recognizer");
                    }
                }
            });
        }
    }

    private boolean hasPermission(final String permission) {
        final Context context = getActivity();
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }

    private void loadQuery(String query) {
        if ((mSearchQuery != null && !mSearchQuery.equals(query)) && !query.trim().isEmpty()
                || (!TextUtils.isEmpty(query) && !query.equals("nil"))) {
            if (NetworkUtils.isNetworkConnected(getActivity())) {
                mSearchQuery = query;
                search(query);
            } else {
                ToastFactory.createWifiErrorToast(getActivity()).show();
            }
        }
    }

    private void search(String query) {
        mResultsAdapter.clear();
        HeaderItem resultsHeader = new HeaderItem(0, getString(R.string.text_search_results));
        mSearchObjectAdapter = new ArrayObjectAdapter(new CardPresenter());
        ListRow listRow = new ListRow(resultsHeader, mSearchObjectAdapter);
        mResultsAdapter.add(listRow);
        mSearchObjectAdapter.clear();
        search();
    }

    private void search() {
        Resources resources = getResources();
        String[] names = resources.getStringArray(R.array.names);
        String[] descriptions = resources.getStringArray(R.array.descriptions);
        String[] images = resources.getStringArray(R.array.images);

        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            photos.add(new Photo(names[i], descriptions[i], images[i]));
        }

        mSearchContentPresenter.search(photos);
    }

    @Override
    public void show(List<Photo> photos) {
        mSearchObjectAdapter.addAll(0, photos);
    }

    @Override
    public void showError() {
        // show loading error state here
        String errorMessage = getString(R.string.error_message_generic);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
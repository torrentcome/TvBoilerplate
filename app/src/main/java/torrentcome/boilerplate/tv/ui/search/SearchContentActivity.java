package torrentcome.boilerplate.tv.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import torrentcome.boilerplate.tv.R;

import torrentcome.boilerplate.tv.ui.base.BaseActivity;

public class SearchContentActivity extends BaseActivity {
    private SearchContentFragment searchContentFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchContentActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchContentFragment = (SearchContentFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
    }

    @Override
    public boolean onSearchRequested() {
        if (searchContentFragment.hasResults()) {
            startActivity(new Intent(this, SearchContentActivity.class));
        } else {
            searchContentFragment.startRecognition();
        }
        return true;
    }

}
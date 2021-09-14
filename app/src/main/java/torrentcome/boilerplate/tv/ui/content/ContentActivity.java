package torrentcome.boilerplate.tv.ui.content;

import android.os.Bundle;
import android.widget.FrameLayout;

import torrentcome.boilerplate.tv.R;

import torrentcome.boilerplate.tv.ui.base.BaseActivity;
import torrentcome.boilerplate.tv.ui.search.SearchContentActivity;

public class ContentActivity extends BaseActivity {

    FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame_container);
        getFragmentManager().beginTransaction().replace(frameLayout.getId(), ContentFragment.newInstance()).commit();
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(SearchContentActivity.getStartIntent(this));
        return true;
    }

}
package torrentcome.boilerplate.tv.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import torrentcome.boilerplate.tv.TvBoilerplateApp;
import torrentcome.boilerplate.tv.di.component.ActivityComponent;
import torrentcome.boilerplate.tv.di.component.DaggerActivityComponent;
import torrentcome.boilerplate.tv.di.module.ActivityModule;

public class BaseActivity extends Activity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(TvBoilerplateApp.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

}
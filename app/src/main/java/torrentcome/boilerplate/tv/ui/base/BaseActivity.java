package torrentcome.boilerplate.tv.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity {

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
}
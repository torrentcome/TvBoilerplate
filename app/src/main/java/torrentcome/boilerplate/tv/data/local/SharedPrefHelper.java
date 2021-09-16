package torrentcome.boilerplate.tv.data.local;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import javax.inject.Inject;

public class SharedPrefHelper {
    private static final String PREF_KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPrefHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void clear(SharedPreferences mPref) {
        mPref.edit().clear().apply();
    }

    public void putAccessToken(SharedPreferences mPref, String accessToken) {
        mPref.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Nullable
    public String getAccessToken(SharedPreferences mPref) {
        return mPref.getString(PREF_KEY_ACCESS_TOKEN, null);
    }
}

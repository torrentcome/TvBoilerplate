package torrentcome.boilerplate.tv.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import torrentcome.boilerplate.tv.di.AppScope;

@Module
public class SharedPrefModule {

    public static final String PREF_FILE_NAME = "tv_boilerplate";

    @Provides
    @AppScope
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

}

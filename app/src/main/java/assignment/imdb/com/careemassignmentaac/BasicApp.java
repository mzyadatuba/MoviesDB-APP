/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import assignment.imdb.com.careemassignmentaac.db.AppDatabase;
import timber.log.Timber;

public class BasicApp extends Application {

    AppExecutors mAppExecutors;
    public static Context context;
    private static BasicApp instance = null;
    static private FirebaseRemoteConfig mFireBaseRemoteConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
        context = getApplicationContext();
        FirebaseApp.initializeApp(this);
        initFireBaseRemoteConfig();
        invalidateFireBaseRemoteConfigUpdate();
    }


    public static BasicApp getInstance() {
        if (instance == null) {
            instance = new BasicApp();
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }

    public DataRepository getRepository(int movieId) {
        return DataRepository.getInstance(getDatabase(), movieId);
    }

    private void initFireBaseRemoteConfig() {
        mFireBaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFireBaseRemoteConfig.setConfigSettings(configSettings);
        mFireBaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    public FirebaseRemoteConfig getFireBaseRemoteConfig() {
        if (mFireBaseRemoteConfig != null) {
            return mFireBaseRemoteConfig;
        } else {
            initFireBaseRemoteConfig();
            return mFireBaseRemoteConfig;
        }
    }

    private void invalidateFireBaseRemoteConfigUpdate() {

        long cacheExpiration = 3600; // Default is 1 hour
        if (BasicApp.getInstance()
                .getFireBaseRemoteConfig()
                .getInfo()
                .getConfigSettings()
                .isDeveloperModeEnabled()) {
            cacheExpiration = Long.parseLong(mFireBaseRemoteConfig.getString(Constants.FBRC_CACHE_EXPIRATION));
        }
        mFireBaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) { // for checking only
                            mFireBaseRemoteConfig.activateFetched();
                            Timber.tag("_FireBase: ").v("Remote config fetched & activated successfully - %s", System.currentTimeMillis());
                            String baseURL = mFireBaseRemoteConfig.getString(Constants.FBRC_API_BASE_URL);
                            String apiKey = mFireBaseRemoteConfig.getString(Constants.FBRC_API_AUTH_KEY);
                            String cache = mFireBaseRemoteConfig.getString(Constants.FBRC_CACHE_EXPIRATION);
                            Timber.tag("_FireBase: URL").v(baseURL);
                            Timber.tag("_FireBase: KEY").v(apiKey);
                            Timber.tag("_FireBase: cache").v(cache);
                        } else {
                            Timber.tag("_FireBase: ").v("Remote config fetch is failed - %s", System.currentTimeMillis());
                        }
                    }
                });
    }

}

/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import assignment.imdb.com.careemassignmentaac.AppExecutors;
import assignment.imdb.com.careemassignmentaac.Constants;
import assignment.imdb.com.careemassignmentaac.db.dao.CountersDao;
import assignment.imdb.com.careemassignmentaac.db.dao.MovieDao;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieCounterEntity;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

@Database(entities = {MovieEntity.class, MovieCounterEntity.class}
        , version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;
    @VisibleForTesting
    public static final String DATABASE_NAME = Constants.ROOM_DB_NAME;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }


    public abstract MovieDao movieDao();

    public abstract CountersDao countersDao();

    /**
     * Build the database. only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                // Add a delay to simulate a long-running operation
                                AppDatabase database = AppDatabase.getInstance(appContext, executors);
                                database.setDatabaseCreated();
                            }
                        });
                    }
                }).build();
    }


    /**
     * Check whether the database already exists and expose it via getDatabaseCreated()
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    /**
     * Optional in case the app. should clear the cached data upon lunching
     */
    private static void clearData(final AppDatabase database) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                database.movieDao().deleteAllMovies();
            }
        });
    }
}


/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import assignment.imdb.com.careemassignmentaac.api.APIService;
import assignment.imdb.com.careemassignmentaac.api.RetrofitFactory;
import assignment.imdb.com.careemassignmentaac.api.apimodels.MovieDetailsResponse;
import assignment.imdb.com.careemassignmentaac.api.apimodels.MovieResponse;
import assignment.imdb.com.careemassignmentaac.db.AppDatabase;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieCounterEntity;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DataRepository {

    private static DataRepository sInstance;
    private AppDatabase mDatabase;
    private MediatorLiveData<List<MovieEntity>> mObservableMovies;
    private MediatorLiveData<MovieEntity> mObservableMovieDetails;
    private MediatorLiveData<MovieCounterEntity> mObservableCounters;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableMovies = new MediatorLiveData<>();
        mObservableMovies.addSource(mDatabase.movieDao().loadAllMovies(), new Observer<List<MovieEntity>>() {
            // Observe changes from repo
            @Override
            public void onChanged(List<MovieEntity> movieEntities) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableMovies.postValue(movieEntities); // Notify observers
                }
            }
        });


        mObservableCounters = new MediatorLiveData<>();
        mObservableCounters.addSource(mDatabase.countersDao().loadCounters(), new Observer<MovieCounterEntity>() {
            // Observe changes from repo
            @Override
            public void onChanged(MovieCounterEntity movieCounterEntity) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableCounters.postValue(movieCounterEntity); // Notify observers
                }
            }
        });
    }

    private DataRepository(final AppDatabase database, int movieId) {
        mDatabase = database;
        mObservableMovieDetails = new MediatorLiveData<>();
        mObservableMovieDetails.addSource(mDatabase.movieDao().loadMovieById(movieId), new Observer<MovieEntity>() {
            // Observe changes from repo
            @Override
            public void onChanged(MovieEntity movieEntities) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableMovieDetails.postValue(movieEntities); // Notify observers
                }
            }
        });
    }


    public static DataRepository getInstance(final AppDatabase database, int movieId) {
        synchronized (DataRepository.class) {
            return new DataRepository(database, movieId);
        }
    }

    public static DataRepository getInstance(final AppDatabase database) {
        synchronized (DataRepository.class) {
            if (sInstance == null) {
                sInstance = new DataRepository(database);
            }
        }

        return sInstance;
    }

    public LiveData<List<MovieEntity>> getMovies() {
        return mObservableMovies;
    }

    public void loadMoviesByDate(final String movieReleaseRate) {
        mObservableMovies.removeSource(mDatabase.movieDao().loadAllMovies());
        mObservableMovies.addSource(mDatabase.movieDao().loadMoviesByDate(movieReleaseRate), new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> movieEntities) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableMovies.postValue(movieEntities); // Notify observers
                }
            }
        });
    }

    public void clearFilterObservable(final String movieReleaseRate) {
        mObservableMovies.removeSource(mDatabase.movieDao().loadMoviesByDate(movieReleaseRate));
        mObservableMovies.addSource(mDatabase.movieDao().loadAllMovies(), new Observer<List<MovieEntity>>() {
            // Observe changes from repo
            @Override
            public void onChanged(List<MovieEntity> movieEntities) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableMovies.postValue(movieEntities); // Notify observers
                }
            }
        });
    }

    public void loadMore(int page) {

        final AppExecutors mAppExecutors = new AppExecutors();
        RetrofitFactory.getClient().create(APIService.class)
                .getUpComingMovies(BasicApp.getInstance().getFireBaseRemoteConfig().getString(Constants.FBRC_API_AUTH_KEY), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v("", "");
                    }

                    @Override
                    public void onNext(MovieResponse movieResponseLiveData) {
                        assert movieResponseLiveData != null;

                        Timber.v("onNext");
                        final List<MovieEntity> movies;
                        final MovieCounterEntity counters = new MovieCounterEntity();
                        movies = MovieResponse.getMappedMoviesList(movieResponseLiveData.getResults());
                        counters.setUid(1);
                        counters.setPage(movieResponseLiveData.getPage());
                        counters.setTotalPages(movieResponseLiveData.getTotal_pages());
                        counters.setTotalResults(movieResponseLiveData.getTotal_results());

                        mAppExecutors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDatabase.runInTransaction(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDatabase.movieDao().insertAll(movies);
                                        mDatabase.countersDao().insert(counters);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.v("onError: %s", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("onComplete");
                    }
                });
    }


    public LiveData<MovieCounterEntity> getMoviesCounters() {
        return mObservableCounters;
    }


    public LiveData<MovieEntity> getMovieDetails() {
        return mObservableMovieDetails;
    }

    public void loadMovieDetails(final int movieId) {
        final AppExecutors mAppExecutors = new AppExecutors();
        RetrofitFactory.getClient().create(APIService.class)
                .getMovieDetails(movieId, BasicApp.getInstance().getFireBaseRemoteConfig().getString(Constants.FBRC_API_AUTH_KEY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<MovieDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final MovieDetailsResponse movieDetailsResponse) {
                        assert movieDetailsResponse != null;

                        Timber.v("onNext");
                        mAppExecutors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDatabase.runInTransaction(new Runnable() {
                                    @Override
                                    public void run() {
                                        MovieEntity entity = MovieDetailsResponse.getMappedMovie(movieDetailsResponse);
                                        mDatabase.movieDao().update(entity.getImdbId()
                                                , entity.getOverview()
                                                , entity.getRevenue()
                                                , entity.getRuntime()
                                                , entity.getVote_count()
                                                , movieId);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.v("onError: %s", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Timber.v("onComplete");
                    }
                });
    }

}

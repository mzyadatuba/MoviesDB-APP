/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieCounterEntity;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

public class MovieListViewModel extends AndroidViewModel {

    private MediatorLiveData<List<MovieEntity>> mObservableMovies;
    private final MediatorLiveData<MovieCounterEntity> mObservableCounters;
        private final Application mApplication;

    public MovieListViewModel(Application application) {
        super(application);
        mApplication = application;
        mObservableMovies = new MediatorLiveData<>();
        LiveData<List<MovieEntity>> movies = ((BasicApp) application).getRepository()
                .getMovies();
        mObservableMovies.addSource(movies, new Observer<List<MovieEntity>>() { // Observe changes from repo
            @Override
            public void onChanged(List<MovieEntity> value) {
                mObservableMovies.postValue(value); // Notify observers
            }
        });

        mObservableCounters = new MediatorLiveData<>();
        LiveData<MovieCounterEntity> counters = ((BasicApp) application).getRepository()
                .getMoviesCounters();
        mObservableCounters.addSource(counters, new Observer<MovieCounterEntity>() {
            @Override
            public void onChanged(@Nullable MovieCounterEntity movieCounterEntity) {
                mObservableCounters.postValue(movieCounterEntity);
            }
        });

    }

    public LiveData<List<MovieEntity>> getMovies() {
        return mObservableMovies;
    }

    public void loadMore(int page) {
        ((BasicApp) mApplication).getRepository().loadMore(page);
    }

    public void loadMoviesByDate(String date) {
        ((BasicApp) mApplication).getRepository().loadMoviesByDate(date);
    }

    public void clearFilterObservable(String date){
        ((BasicApp) mApplication).getRepository().clearFilterObservable(date);
    }
    public LiveData<MovieCounterEntity> getCounters() {
        return mObservableCounters;
    }
}

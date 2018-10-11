/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

public class MovieDetailsViewModel extends AndroidViewModel {

    private final MediatorLiveData<MovieEntity> mObservableProduct;
    private final int mMovieId;
    private final Application mApplication;


    public MovieDetailsViewModel(@NonNull Application application, int mMovieId) {
        super(application);
        this.mMovieId = mMovieId;
        this.mApplication = application;
        mObservableProduct = new MediatorLiveData<>();

        LiveData<MovieEntity> details = ((BasicApp) application).getRepository(mMovieId)
                .getMovieDetails();
        mObservableProduct.addSource(details, new Observer<MovieEntity>() {
            @Override
            public void onChanged(@Nullable MovieEntity movieEntity) {
                mObservableProduct.postValue(movieEntity);
            }
        });
    }

    public LiveData<MovieEntity> getObservableMovie() {
        return mObservableProduct;
    }

    public void loadMovieDetails(int movieId) {
        ((BasicApp) mApplication).getRepository(movieId).loadMovieDetails(movieId);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mProductId;


        public Factory(@NonNull Application application, int productId) {
            mApplication = application;
            mProductId = productId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MovieDetailsViewModel(mApplication, mProductId);
        }
    }

}

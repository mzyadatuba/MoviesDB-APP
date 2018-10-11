/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.Constants;
import assignment.imdb.com.careemassignmentaac.R;
import assignment.imdb.com.careemassignmentaac.databinding.MovieDetialsActivityBinding;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;
import assignment.imdb.com.careemassignmentaac.viewmodel.MovieDetailsViewModel;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE_ID = Constants.MOVIE_ID;
    MovieDetailsViewModel viewModel = null;
    MovieDetialsActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.movie_detials_activity);
        mBinding.setClickHandler(this);
        MovieDetailsViewModel.Factory factory = new MovieDetailsViewModel.Factory(BasicApp.getInstance(),
                getIntent().getIntExtra(KEY_MOVIE_ID, 0));

        viewModel = ViewModelProviders.of(this, factory)
                .get(MovieDetailsViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(final MovieDetailsViewModel viewModel) {
        viewModel.getObservableMovie().observe(this, new Observer<MovieEntity>() {
            @Override
            public void onChanged(@Nullable MovieEntity movieEntity) {
                assert movieEntity != null;
                if (TextUtils.isEmpty(movieEntity.getOverview())) {
                    viewModel.loadMovieDetails(getIntent().getIntExtra(KEY_MOVIE_ID, 0));
                } else {
                    mBinding.setMovieDetails(movieEntity);
                }
            }
        });
    }


    public void setOnClick(View view) {
        String url = Constants.IMDB_BASE_URL + mBinding.getMovieDetails().getImdbId();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }
}

/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.R;
import assignment.imdb.com.careemassignmentaac.databinding.MoviesListFragmentBinding;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieCounterEntity;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;
import assignment.imdb.com.careemassignmentaac.ui.utils.EndlessScrollListener;
import assignment.imdb.com.careemassignmentaac.viewmodel.MovieDetailsViewModel;
import assignment.imdb.com.careemassignmentaac.viewmodel.MovieListViewModel;

public class MovieListFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    public static final String TAG = "MovieListViewModel";
    private MoviesAdapter mMoviesAdapter;
    private MoviesListFragmentBinding mBinding;
    MovieListViewModel viewModel = null;
    private int currentPageNumber = 1;
    boolean isFilterActive = false;
    String selectedDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.movies_list_fragment, container, false);
        mBinding.setIsLoading(true);
        mBinding.setMoveList(this);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoviesAdapter = new MoviesAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mBinding.moviesList.setLayoutManager(layoutManager);
        mBinding.moviesList.setAdapter(mMoviesAdapter);
        mBinding.moviesList.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isFilterActive) return;

                Snackbar snackbar = Snackbar.make(view, R.string.load_next, Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();

                currentPageNumber++;
                viewModel.loadMore(currentPageNumber);
            }
        });

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(final MovieListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getMovies().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(List<MovieEntity> movieEntities) {
                if (movieEntities != null) {
                    if (movieEntities.size() == 0 && !isFilterActive) {
                        viewModel.loadMore(1);
                    } else {
                        mBinding.setIsLoading(false);
                        mMoviesAdapter.setMoviesList(movieEntities);
                    }

                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });

        viewModel.getCounters().observe(this, new Observer<MovieCounterEntity>() {
            @Override
            public void onChanged(MovieCounterEntity movieCounterEntity) {
                if (movieCounterEntity != null) {
                    currentPageNumber = movieCounterEntity.getPage();
                    mBinding.tvTotalResults.setText(new StringBuilder()
                            .append(getString(R.string.total_results))
                            .append(" ")
                            .append(movieCounterEntity.getTotalResults()).toString());
                    mBinding.tvPageNumber.setText(new StringBuilder()
                            .append(getString(R.string.page_number))
                            .append(" ")
                            .append(movieCounterEntity.getPage()).toString());
                } else {
                    mBinding.tvTotalResults.setText(getString(R.string.def_total_results));
                    mBinding.tvPageNumber.setText(getString(R.string.def_page_number));
                }
            }
        });
    }

    public void onFilterClicked(View view) {
        Calendar now = Calendar.getInstance();

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance(
                MovieListFragment.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
        );
        dpd.show(Objects.requireNonNull(getActivity()).getFragmentManager(), getString(R.string.filter_by_date));
        DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());
        ArrayList<Calendar> days = new ArrayList<>();
        for (MovieEntity movie : Objects.requireNonNull(viewModel.getMovies().getValue())) {
            Calendar event = Calendar.getInstance();
            try {
                Date date = formatter.parse(movie.getReleaseDate());
                event.setTime(date);
                days.add(event);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar[] highlightedDays = new Calendar[days.size()];
        for (int i = 0; i < days.size(); i++) {
            highlightedDays[i] = days.get(i);
        }
        dpd.setHighlightedDays(highlightedDays);
    }


    public void onClearFilterClicked(View view) {
        viewModel.clearFilterObservable(selectedDate);
        isFilterActive = false;
        mBinding.tvClearFilter.setVisibility(View.GONE);
        Toast.makeText(getContext(), getString(R.string.remove_filter_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedDate = year + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        isFilterActive = true;
        viewModel.loadMoviesByDate(selectedDate);
        mBinding.tvClearFilter.setVisibility(View.VISIBLE);
    }
}

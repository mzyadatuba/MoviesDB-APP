/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.R;
import assignment.imdb.com.careemassignmentaac.databinding.MovieItemBinding;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    List<MovieEntity> mMoviesList = new ArrayList<>();

    public MoviesAdapter() {
    }


    public void setMoviesList(final List<MovieEntity> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MovieItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.movie_item,
                        viewGroup, false);

        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int i) {
        holder.binding.setMovie(mMoviesList.get(i));
        holder.binding.executePendingBindings();
    }

    @Override
    public long getItemId(int position) {
        return mMoviesList.get(position).getUid();
    }

    @Override
    public int getItemCount() {
        return mMoviesList == null ? 0 : mMoviesList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final MovieItemBinding binding;

        public MovieViewHolder(final MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieEntity movie = binding.getMovie();
                    Intent intent = new Intent(BasicApp.getInstance().getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.KEY_MOVIE_ID, movie.getMovieID());
                    BasicApp.getInstance().getContext().startActivity(intent);
                }
            });
        }
    }
}

/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.api.apimodels;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

public class MovieResponse {


    @SerializedName("results")
    ArrayList<MovieListResponse> results;
    @SerializedName("page")
    int page;
    @SerializedName("total_results")
    int total_results;
    @SerializedName("total_pages")
    int total_pages;

    public ArrayList<MovieListResponse> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieListResponse> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }


    @SuppressLint("CheckResult")
    public static ArrayList<MovieEntity> getMappedMoviesList(ArrayList<MovieListResponse> remoteList) {
        ArrayList<MovieEntity> result = new ArrayList<>();
        for (MovieListResponse movieListResponse : remoteList) {
            MovieEntity movie = new MovieEntity();
            movie.setMovieID(movieListResponse.getMovieID());
            movie.setMovieTitle(movieListResponse.getMovieTitle());
            movie.setReleaseDate(movieListResponse.getReleaseDate());
            movie.setPosterPath(movieListResponse.getPosterPath());
            movie.setBackdropPath(movieListResponse.getBackdropPath());
            result.add(movie);
        }
        return result;
    }

}

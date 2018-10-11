
/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.api.apimodels;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

public class MovieDetailsResponse {
    @SerializedName("id")
    private int movieID;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("imdb_id")
    private String imdbID;
    @SerializedName("overview")
    private String overview;
    @SerializedName("revenue")
    private long revenue;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("vote_count")
    private int vote_count;

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    @SuppressLint("CheckResult")
    public static MovieEntity getMappedMovie(MovieDetailsResponse remoteMovie) {
        MovieEntity movie = new MovieEntity();
        movie.setMovieID(remoteMovie.getMovieID());
        movie.setMovieTitle(remoteMovie.getMovieTitle());
        movie.setReleaseDate(remoteMovie.getReleaseDate());
        movie.setPosterPath(remoteMovie.getPosterPath());
        movie.setBackdropPath(remoteMovie.getBackdropPath());
        movie.setImdbId(remoteMovie.getImdbID());
        movie.setOverview(remoteMovie.getOverview());
        movie.setRevenue(remoteMovie.getRevenue());
        movie.setVote_count(remoteMovie.getVote_count());
        movie.setRuntime(remoteMovie.getRuntime());
        return movie;
    }
}

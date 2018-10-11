/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.R;

@Entity(tableName = "movies")
public class MovieEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "movie_id")
    private int movieID;
    @ColumnInfo(name = "movie_title")
    private String movieTitle;
    @ColumnInfo(name = "movie_poster")
    private String posterPath;
    @ColumnInfo(name = "movie_backdrop")
    private String backdropPath;
    @ColumnInfo(name = "movie_release_date")
    private String releaseDate;
    @ColumnInfo(name = "imdb_id")
    private String imdbId;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "revenue")
    private long revenue;
    @ColumnInfo(name = "runtime")
    private int runtime;
    @ColumnInfo(name = "vote_count")
    private int vote_count;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

    public MovieEntity() {
    }


    @BindingAdapter({"backdropPath"})
    public static void loadBackdropImage(ImageView view, String imageUrl) {
        RequestOptions iv_backdropOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        ;
        Glide.with(BasicApp.context)
                .load("https://image.tmdb.org/t/p/original/" + imageUrl)
                .apply(iv_backdropOptions)
                .into(view);

    }

    @BindingAdapter({"posterPath"})
    public static void loadPosterImage(ImageView view, String imageUrl) {
        RequestOptions iv_posterOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(BasicApp.context)
                .load("https://image.tmdb.org/t/p/original/" + imageUrl)
                .apply(iv_posterOptions)
                .into(view);

    }
}

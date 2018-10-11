/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

@Dao
public abstract class MovieDao {
    @Query("SELECT * FROM movies")
    public abstract LiveData<List<MovieEntity>> loadAllMovies();

    @Query("DELETE FROM movies")
    public abstract void deleteAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<MovieEntity> movies);

    @Query("select * from movies where movie_release_date = :movieReleaseDate")
    public abstract LiveData<List<MovieEntity>> loadMoviesByDate(String movieReleaseDate);

    @Query("select * from movies where movie_id = :movieId")
    public abstract LiveData<MovieEntity> loadMovieById(int movieId);

    @Query("UPDATE movies SET imdb_id = :imdb_id" +
            ", overview = :overview" +
            ", revenue = :revenue" +
            ", runtime = :runtime" +
            ", vote_count = :vote_count WHERE movie_id = :movieId")
    public abstract void update(String imdb_id
            , String overview
            , long revenue
            , int runtime
            , int vote_count
            ,int movieId);
}

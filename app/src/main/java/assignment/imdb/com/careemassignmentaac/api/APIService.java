/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.api;

import assignment.imdb.com.careemassignmentaac.api.apimodels.MovieDetailsResponse;
import assignment.imdb.com.careemassignmentaac.api.apimodels.MovieResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie/upcoming")
    Observable<MovieResponse> getUpComingMovies(@Query("api_key") String apiKey
            , @Query("page") int page);

    @GET("movie/{movie_id}")
    Observable<MovieDetailsResponse> getMovieDetails(@Path("movie_id") int id
            , @Query("api_key") String apiKey);
}

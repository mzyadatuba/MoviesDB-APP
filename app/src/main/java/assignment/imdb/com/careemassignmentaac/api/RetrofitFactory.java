
/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import assignment.imdb.com.careemassignmentaac.BasicApp;
import assignment.imdb.com.careemassignmentaac.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BasicApp.getInstance().getFireBaseRemoteConfig().getString(Constants.FBRC_API_BASE_URL))
                    .build();
        }
        return retrofit;
    }
}

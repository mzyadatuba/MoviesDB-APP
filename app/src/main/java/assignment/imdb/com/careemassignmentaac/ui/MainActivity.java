/**
 * Created by MOHAMMD Al-ZYADAT.
 */

package assignment.imdb.com.careemassignmentaac.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import androidx.annotation.Nullable;
import assignment.imdb.com.careemassignmentaac.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            MovieListFragment fragment = new MovieListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, MovieListFragment.TAG).commit();
        }

    }

}

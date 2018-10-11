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

import assignment.imdb.com.careemassignmentaac.db.entity.MovieCounterEntity;
import assignment.imdb.com.careemassignmentaac.db.entity.MovieEntity;

@Dao
public abstract class CountersDao {
    @Query("SELECT * FROM counters Limit 1")
    public abstract LiveData<MovieCounterEntity> loadCounters();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(MovieCounterEntity counters);
}

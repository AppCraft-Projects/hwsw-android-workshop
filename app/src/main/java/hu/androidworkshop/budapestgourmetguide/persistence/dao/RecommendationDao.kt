package hu.androidworkshop.budapestgourmetguide.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel

@Dao
interface RecommendationDao {

    @Query("SELECT * FROM recommendations")
    fun getAll() : LiveData<List<RecommendationModel>>

    @Query("SELECT * FROM recommendations WHERE recommendations.id = :id")
    fun getById(id: Int) : RecommendationModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(model: RecommendationModel)

    @Query("DELETE FROM recommendations")
    fun deleteAll()
}

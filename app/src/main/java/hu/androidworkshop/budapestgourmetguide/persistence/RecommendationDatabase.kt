package hu.androidworkshop.budapestgourmetguide.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel
import hu.androidworkshop.budapestgourmetguide.persistence.dao.RecommendationDao

@Database(entities = arrayOf(RecommendationModel::class), version = 1)
abstract class RecommendationDatabase : RoomDatabase() {
    abstract fun getRecommendationDao(): RecommendationDao

    companion object {
        @JvmStatic val DATABASE_NAME = "recommendations"
    }
}

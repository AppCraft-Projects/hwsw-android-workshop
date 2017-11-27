package hu.androidworkshop.budapestgourmetguide.repository

import android.util.Log
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel
import hu.androidworkshop.budapestgourmetguide.network.BGGApiDefinition
import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Repository<out T, in IdType> {
    fun getAll(callback: (List<T>?) -> Unit)
    fun getById(id: IdType) : T?
}

class RecommendationRepository(private val apiDefinition: BGGApiDefinition, private val databaseHelper: RecommendationDatabaseHelper) : Repository<RecommendationModel,Int> {

    companion object {
        @JvmStatic val TAG: String = RecommendationRepository::class.java.simpleName
    }

    override fun getAll(callback: (List<RecommendationModel>?) -> Unit) {
        apiDefinition.getRecommendations().enqueue(object: Callback<List<RecommendationModel>> {
            override fun onResponse(call: Call<List<RecommendationModel>>?, response: Response<List<RecommendationModel>>?) {
                val items = response?.body()
                items?.forEach {
                    databaseHelper.addRecommendation(it)
                }
                callback(items)
            }

            override fun onFailure(call: Call<List<RecommendationModel>>?, t: Throwable?) {
                Log.e(TAG, "Error while fetching recommendations")
                callback(null)
            }
        })
    }

    override fun getById(id: Int): RecommendationModel? = databaseHelper.getRecommendationById(id)
}
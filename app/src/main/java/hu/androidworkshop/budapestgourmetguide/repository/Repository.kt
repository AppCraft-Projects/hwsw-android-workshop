package hu.androidworkshop.budapestgourmetguide.repository

import android.util.Log
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel
import hu.androidworkshop.budapestgourmetguide.network.BGGApiDefinition
import hu.androidworkshop.budapestgourmetguide.persistence.dao.RecommendationDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface Repository<out T, in IdType> {
    //TODO: Change from callback to return LiveData<List<T>>
    fun getAll(callback: (List<T>?) -> Unit)
    fun getById(id: IdType) : T?
}

class RecommendationRepository(private val apiDefinition: BGGApiDefinition, private val recommendationDao: RecommendationDao) : Repository<RecommendationModel,Int> {

    companion object {
        @JvmStatic val TAG: String = RecommendationRepository::class.java.simpleName
    }

    //TODO: Chage
    override fun getAll(callback: (List<RecommendationModel>?) -> Unit) {
        apiDefinition.getRecommendations().enqueue(object: Callback<List<RecommendationModel>> {
            override fun onResponse(call: Call<List<RecommendationModel>>?, response: Response<List<RecommendationModel>>?) {
                val items = response?.body()
                items?.forEach {
                    recommendationDao.add(it)
                }
                callback(items)
            }

            override fun onFailure(call: Call<List<RecommendationModel>>?, t: Throwable?) {
                Log.e(TAG, "Error while fetching recommendations")
                callback(null)
            }
        })
    }

    //TODO: Use the given RecommendationDao
    override fun getById(id: Int): RecommendationModel? = recommendationDao.getById(id)
}
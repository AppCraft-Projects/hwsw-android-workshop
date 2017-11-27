package hu.androidworkshop.budapestgourmetguide.network

import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel
import retrofit2.Call
import retrofit2.http.GET

interface BGGApiDefinition {
    @GET("restaurants")
    fun getRecommendations() : Call<List<RecommendationModel>>
}

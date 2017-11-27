package hu.androidworkshop.budapestgourmetguide.network

import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BGGApiDefinition {
    @GET("restaurants")
    fun getRecommendations() : Call<List<RecommendationModel>>

    @POST("restaurants")
    fun addRestaurant(@Body recommendation : RecommendationModel) : Call<RecommendationModel>
}

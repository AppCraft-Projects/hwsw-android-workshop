package hu.androidworkshop.budapestgourmetguide.model

import com.google.gson.annotations.SerializedName

data class RecommendationModel(
        val id: Int,
        val name: String,
        @SerializedName("short-desc") val shortDescription: String,
        @SerializedName("image-url") val imageURL: String,
        val user: UserModel,
        val liked: Boolean)

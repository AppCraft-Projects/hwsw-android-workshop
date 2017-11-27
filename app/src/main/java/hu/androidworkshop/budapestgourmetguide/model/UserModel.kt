package hu.androidworkshop.budapestgourmetguide.model

import com.google.gson.annotations.SerializedName

data class UserModel(@SerializedName("first-name") val firstName: String, @SerializedName("last-name") val lastName: String)

package hu.androidworkshop.budapestgourmetguide.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recommendations")
data class RecommendationModel(
        @PrimaryKey val id: Int,
        val name: String,
        @SerializedName("short-desc") val shortDescription: String,
        @SerializedName("image-url") val imageURL: String,
        @Embedded val user: UserModel,
        val liked: Boolean)

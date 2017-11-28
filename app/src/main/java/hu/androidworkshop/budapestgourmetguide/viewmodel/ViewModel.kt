package hu.androidworkshop.budapestgourmetguide.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel
import hu.androidworkshop.budapestgourmetguide.repository.Repository

class NearbyViewModel(application: Application) : AndroidViewModel(application) {
    val recommendations: LiveData<List<RecommendationModel>>
    get() = repository.getAll()

    lateinit var repository : Repository<RecommendationModel,Int>
}

class RecommendationDetailViewModel(application: Application) : AndroidViewModel(application) {
    val recommendation: RecommendationModel
    get() = repository.getById(id)!!

    lateinit var repository : Repository<RecommendationModel,Int>
    var id: Int = -1
}

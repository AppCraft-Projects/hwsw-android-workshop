package hu.androidworkshop.budapestgourmetguide;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel;
import hu.androidworkshop.budapestgourmetguide.network.BGGApiDefinition;
import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabase;
import hu.androidworkshop.budapestgourmetguide.persistence.dao.RecommendationDao;
import hu.androidworkshop.budapestgourmetguide.repository.RecommendationRepository;
import hu.androidworkshop.budapestgourmetguide.repository.Repository;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GourmetApplication extends Application {

    private Repository<RecommendationModel,Integer> repository;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .connectTimeout(60L, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().create();

        BGGApiDefinition apiDefinition = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_BASE_URL)
                .build()
                .create(BGGApiDefinition.class);

        RecommendationDao recommendationDao = Room
                .databaseBuilder(this, RecommendationDatabase.class, RecommendationDatabase.getDATABASE_NAME())
                .allowMainThreadQueries()
                .build()
                .getRecommendationDao();

        repository = new RecommendationRepository(apiDefinition, recommendationDao);
        recommendationDao.deleteAll();
    }

    public Repository<RecommendationModel,Integer> getRepository() {
        return repository;
    }
}

package hu.androidworkshop.budapestgourmetguide;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import hu.androidworkshop.budapestgourmetguide.network.BGGApiDefinition;
import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabaseHelper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GourmetApplication extends Application {

    private BGGApiDefinition apiDefinition;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .connectTimeout(60L, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().create();

        apiDefinition = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_BASE_URL)
                .build()
                .create(BGGApiDefinition.class);

        RecommendationDatabaseHelper.getInstance(this).deleteAllPostsAndUsers();
    }

    public BGGApiDefinition getApiClient() {
        return apiDefinition;
    }
}

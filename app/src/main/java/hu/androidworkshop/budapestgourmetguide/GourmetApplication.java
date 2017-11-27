package hu.androidworkshop.budapestgourmetguide;

import android.app.Application;

import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabaseHelper;

public class GourmetApplication extends Application {

    //TODO: Define BGGApiDefinition instance

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Obtain OkHttpClient instance
        //
        //TODO: Obtain Gson instance
        //
        //TODO: Obtain BGGApiDefinition instance

        RecommendationDatabaseHelper.getInstance(this).deleteAllPostsAndUsers();
    }

    //TODO: Define getter for BGGApiDefinition
}

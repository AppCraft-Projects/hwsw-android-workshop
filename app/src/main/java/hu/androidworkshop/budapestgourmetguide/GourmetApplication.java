package hu.androidworkshop.budapestgourmetguide;

import android.app.Application;

import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabaseHelper;

public class GourmetApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RecommendationDatabaseHelper.getInstance(this).deleteAllPostsAndUsers();
    }
}

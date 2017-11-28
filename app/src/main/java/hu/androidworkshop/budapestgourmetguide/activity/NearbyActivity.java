package hu.androidworkshop.budapestgourmetguide.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import hu.androidworkshop.budapestgourmetguide.GourmetApplication;
import hu.androidworkshop.budapestgourmetguide.R;
import hu.androidworkshop.budapestgourmetguide.adapter.NearbyAdapter;
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel;

import static hu.androidworkshop.budapestgourmetguide.activity.RecommendationDetailActivity.RECOMMENDATION_ID_KEY_BUNDLE;

public class NearbyActivity extends AppCompatActivity {

    private static final String TAG = NearbyActivity.class.getClass().getSimpleName();

    private ListView listView;
    private ArrayAdapter<RecommendationModel> adapter;
    private GourmetApplication application;
    //TODO: Define NearbyViewModel

    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, NearbyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        setTitle(R.string.nearby_title);

        application = (GourmetApplication) getApplication();
        //TODO: Obtain NearbyViewModel
        //TODO: Set NearbyViewModel's Repository
        listView = findViewById(R.id.places_listview);

        adapter = new NearbyAdapter(this);


        //TODO: Replace this with ViewModel's observation
        application.getRepository().getAll().observe(this, new Observer<List<RecommendationModel>>() {
            @Override
            public void onChanged(@Nullable List<RecommendationModel> recommendationModels) {
                renderItems(recommendationModels);
            }
        });

        listView.setAdapter(adapter);
    }

    public void itemClicked(RecommendationModel recommendation) {
        Intent intent = RecommendationDetailActivity.newIntent(this);
        intent.putExtra(RECOMMENDATION_ID_KEY_BUNDLE, recommendation.getId());
        ActivityCompat.startActivity(this, intent, null);
    }

    private void renderItems(List<? extends RecommendationModel> recommendationModels) {
        adapter.clear();
        adapter.addAll(recommendationModels);
        adapter.notifyDataSetChanged();
    }
}

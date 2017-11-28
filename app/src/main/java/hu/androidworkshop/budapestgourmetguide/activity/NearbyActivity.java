package hu.androidworkshop.budapestgourmetguide.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import hu.androidworkshop.budapestgourmetguide.viewmodel.NearbyViewModel;

import static hu.androidworkshop.budapestgourmetguide.activity.RecommendationDetailActivity.RECOMMENDATION_ID_KEY_BUNDLE;

public class NearbyActivity extends AppCompatActivity {

    private static final String TAG = NearbyActivity.class.getClass().getSimpleName();

    private ListView listView;
    private ArrayAdapter<RecommendationModel> adapter;
    private GourmetApplication application;
    private NearbyViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(NearbyViewModel.class);
        viewModel.setRepository(application.getRepository());
        listView = findViewById(R.id.places_listview);

        adapter = new NearbyAdapter(this);

        viewModel.getRecommendations().observe(this, new Observer<List<RecommendationModel>>() {
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

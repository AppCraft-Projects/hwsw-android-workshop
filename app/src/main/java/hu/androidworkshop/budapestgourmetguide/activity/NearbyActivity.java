package hu.androidworkshop.budapestgourmetguide.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabaseHelper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static hu.androidworkshop.budapestgourmetguide.activity.RecommendationDetailActivity.RECOMMENDATION_ID_KEY_BUNDLE;

public class NearbyActivity extends AppCompatActivity {

    private static final String TAG = NearbyActivity.class.getClass().getSimpleName();

    private ListView listView;
    private ArrayAdapter<RecommendationModel> adapter;
    private RecommendationDatabaseHelper databaseHelper;
    private GourmetApplication application;
    private ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        databaseHelper = RecommendationDatabaseHelper.getInstance(this);
        listView = findViewById(R.id.places_listview);

        adapter = new NearbyAdapter(this);


        List<RecommendationModel> models = databaseHelper.getRecommendations();
        adapter.addAll(models);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRecommendations();
    }

    private void fetchRecommendations() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        application.getRepository().getAll(new Function1<List<? extends RecommendationModel>, Unit>() {
            @Override
            public Unit invoke(List<? extends RecommendationModel> recommendationModels) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (recommendationModels != null) {
                    renderItems(recommendationModels);
                }
                return null;
            }
        });
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

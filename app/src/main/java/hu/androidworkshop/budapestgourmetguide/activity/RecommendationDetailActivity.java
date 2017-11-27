package hu.androidworkshop.budapestgourmetguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import hu.androidworkshop.budapestgourmetguide.R;
import hu.androidworkshop.budapestgourmetguide.adapter.NearbyAdapter;
import hu.androidworkshop.budapestgourmetguide.cache.ImageCache;
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel;
import hu.androidworkshop.budapestgourmetguide.persistence.RecommendationDatabaseHelper;

public class RecommendationDetailActivity extends AppCompatActivity {

    public static final String RECOMMENDATION_ID_KEY_BUNDLE = "RECOMMENDATION_ID_KEY_BUNDLE";

    public static Intent newIntent(NearbyActivity nearbyActivity) {
        return new Intent(nearbyActivity, RecommendationDetailActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_detail);


        int id = getIntent().getIntExtra(RECOMMENDATION_ID_KEY_BUNDLE, -1);
        RecommendationModel recommendationModel = RecommendationDatabaseHelper.getInstance(this).getRecommendationById(id);

        TextView placeName = findViewById(R.id.place_name);
        placeName.setText(recommendationModel.getName());
        TextView authorInfo = findViewById(R.id.author_info);
        String authorInfoText = recommendationModel.getUser().getFirstName() + " " + recommendationModel.getUser().getLastName();
        authorInfo.setText(authorInfoText);

        ImageView placePhoto = findViewById(R.id.place_photo);
        if (!ImageCache.getInstance().contains(recommendationModel.getImageURL())) {
            new NearbyAdapter.DownloadImageTask(placePhoto).execute(recommendationModel.getImageURL());
        } else {
            placePhoto.setImageBitmap(ImageCache.getInstance().get(recommendationModel.getImageURL()));
        }

        TextView description = findViewById(R.id.place_description);
        description.setText(recommendationModel.getShortDescription());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NearbyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

package hu.androidworkshop.budapestgourmetguide.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hu.androidworkshop.budapestgourmetguide.GourmetApplication;
import hu.androidworkshop.budapestgourmetguide.R;
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel;
import hu.androidworkshop.budapestgourmetguide.viewmodel.RecommendationDetailViewModel;

public class RecommendationDetailActivity extends AppCompatActivity {

    public static final String RECOMMENDATION_ID_KEY_BUNDLE = "RECOMMENDATION_ID_KEY_BUNDLE";

    public static Intent newIntent(NearbyActivity nearbyActivity) {
        return new Intent(nearbyActivity, RecommendationDetailActivity.class);
    }

    private RecommendationDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_detail);


        int id = getIntent().getIntExtra(RECOMMENDATION_ID_KEY_BUNDLE, -1);
        GourmetApplication application = (GourmetApplication) getApplication();
        viewModel = ViewModelProviders.of(this).get(RecommendationDetailViewModel.class);
        viewModel.setRepository(application.getRepository());
        viewModel.setId(id);
        RecommendationModel recommendationModel = viewModel.getRecommendation();

        TextView placeName = findViewById(R.id.place_name);
        placeName.setText(recommendationModel.getName());
        TextView authorInfo = findViewById(R.id.author_info);
        String authorInfoText = recommendationModel.getUser().getFirstName() + " " + recommendationModel.getUser().getLastName();
        authorInfo.setText(authorInfoText);

        ImageView placePhoto = findViewById(R.id.place_photo);
        Picasso.with(this)
                .load(recommendationModel.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(placePhoto);

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

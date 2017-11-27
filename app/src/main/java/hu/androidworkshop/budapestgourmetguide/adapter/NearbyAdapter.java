package hu.androidworkshop.budapestgourmetguide.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import hu.androidworkshop.budapestgourmetguide.R;
import hu.androidworkshop.budapestgourmetguide.activity.NearbyActivity;
import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel;

public class NearbyAdapter extends ArrayAdapter<RecommendationModel> {

    private static final String TAG = NearbyAdapter.class.getSimpleName();

    public NearbyAdapter(@NonNull Context context) {
        super(context, R.layout.recommendation_item);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final RecommendationModel recommendation = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recommendation_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.foodImageView = convertView.findViewById(R.id.place_photo);
            viewHolder.placeName = convertView.findViewById(R.id.place_name);
            viewHolder.authorInfo = convertView.findViewById(R.id.author_info);
            viewHolder.description = convertView.findViewById(R.id.place_description);
            viewHolder.userPhoto = convertView.findViewById(R.id.person_photo);
            viewHolder.detailsButton = convertView.findViewById(R.id.details_button);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.placeName.setText(recommendation.getName());

        String authorInfoText = recommendation.getUser().getFirstName() + " " + recommendation.getUser().getLastName();
        viewHolder.authorInfo.setText(authorInfoText);

        String descriptionText = recommendation.getShortDescription();
        viewHolder.description.setText(descriptionText);

        viewHolder.userPhoto.setImageResource(R.drawable.user_placeholder);

        if (viewHolder.foodImageView != null) {
            //TODO: Replace this to Picasso
            Picasso.with(getContext())
                    .load(recommendation.getImageURL())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(viewHolder.foodImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Successfully loaded image from: " + recommendation.getImageURL());
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "Error downloading image from " + recommendation.getImageURL());
                        }
                    });
        }

        viewHolder.detailsButton.setText(R.string.details_button_title);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NearbyActivity)getContext()).itemClicked(recommendation);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView placeName;
        TextView authorInfo;
        ImageView foodImageView;
        TextView description;
        ImageView userPhoto;
        Button detailsButton;
    }

}

package hu.androidworkshop.budapestgourmetguide.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.androidworkshop.budapestgourmetguide.R;
import hu.androidworkshop.budapestgourmetguide.activity.NearbyActivity;
import hu.androidworkshop.budapestgourmetguide.cache.ImageCache;
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
            if (!ImageCache.getInstance().contains(recommendation.getImageURL())) {
                new DownloadImageTask(viewHolder.foodImageView).execute(recommendation.getImageURL());
            } else {
                viewHolder.foodImageView.setImageBitmap(ImageCache.getInstance().get(recommendation.getImageURL()));
            }
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

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;
        private String url;

        public DownloadImageTask(ImageView imageView) {
            imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            url = strings[0];
            return downloadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (bitmap != null) {
                ImageCache.getInstance().put(url, bitmap);
            }

            if (imageViewWeakReference != null) {
                ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode < 200 && statusCode >= 300) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                Log.e(TAG, "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}

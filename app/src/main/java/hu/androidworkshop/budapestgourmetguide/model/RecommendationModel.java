package hu.androidworkshop.budapestgourmetguide.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RecommendationModel {

    public static final String TAG = RecommendationModel.class.getSimpleName();

    private Integer id;

    private String name;

    private String shortDescription;

    private String imageURL;

    private UserModel user;

    private Boolean liked;

    public Integer getId() {
        return id;
    }

    public RecommendationModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecommendationModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public RecommendationModel setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getImageURL() {
        return imageURL;
    }

    public RecommendationModel setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public UserModel getUser() {
        return user;
    }

    public RecommendationModel setUser(UserModel user) {
        this.user = user;
        return this;
    }

    public Boolean getLiked() {
        return liked;
    }

    public RecommendationModel setLiked(Boolean liked) {
        this.liked = liked;
        return this;
    }

    public RecommendationModel() {}

    public RecommendationModel(JSONObject jsonObject) {
        try {
            if (jsonObject.has("id")) {
                id = Integer.parseInt(jsonObject.getString("id"));
            }
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            if (jsonObject.has("short-desc")) {
                shortDescription = jsonObject.getString("short-desc");
            }
            if (jsonObject.has("image-url")) {
                imageURL = jsonObject.getString("image-url");
            }
            if (jsonObject.has("user")) {
                user = new UserModel(jsonObject.getJSONObject("user"));
            }
            if (jsonObject.has("liked")) {
                liked = jsonObject.getBoolean("liked");
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendationModel that = (RecommendationModel) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!shortDescription.equals(that.shortDescription)) return false;
        if (!imageURL.equals(that.imageURL)) return false;
        if (!user.equals(that.user)) return false;
        return liked.equals(that.liked);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + shortDescription.hashCode();
        result = 31 * result + imageURL.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + liked.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RecommendationModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", user=" + user.toString() +
                ", liked=" + liked +
                '}';
    }
}

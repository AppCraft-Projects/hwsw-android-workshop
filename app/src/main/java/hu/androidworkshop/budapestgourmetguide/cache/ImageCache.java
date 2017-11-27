package hu.androidworkshop.budapestgourmetguide.cache;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class ImageCache {

    private static final ImageCache ourInstance = new ImageCache();

    public static ImageCache getInstance() {
        return ourInstance;
    }

    private ImageCache() {
    }

    private Map<String,Bitmap> cache = new HashMap<>();

    public void put(String url, Bitmap bitmap) {
        if (!cache.containsKey(url)) {
            cache.put(url, bitmap);
        }
    }

    public Bitmap get(String url) {
        if (cache.containsKey(url)) {
            return cache.get(url);
        }
        return null;
    }

    public boolean contains(String url) {
        return cache.containsKey(url);
    }
}

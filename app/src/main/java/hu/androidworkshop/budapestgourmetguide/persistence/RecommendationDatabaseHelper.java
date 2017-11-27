package hu.androidworkshop.budapestgourmetguide.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.androidworkshop.budapestgourmetguide.model.RecommendationModel;
import hu.androidworkshop.budapestgourmetguide.model.UserModel;

public class RecommendationDatabaseHelper extends SQLiteOpenHelper {

    private static RecommendationDatabaseHelper instance;

    private static final String TAG = RecommendationDatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "recommendations";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_RECOMMENDATIONS = "recommendations";
    private static final String TABLE_USERS = "users";

    private static final String KEY_RECOMMENDATION_ID = "id";
    private static final String KEY_RECOMMENDATION_NAME = "name";
    private static final String KEY_RECOMMENDATION_SHORT_DESCRIPTION = "short_desc";
    private static final String KEY_RECOMMENDATION_IMAGE_URL = "img_url";
    private static final String KEY_RECOMMENDATION_LIKED = "liked";
    private static final String KEY_RECOMMENDATION_USER_ID = "user_id";

    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_FIRST_NAME = "user_first_name";
    private static final String KEY_USER_LAST_NAME = "user_last_name";

    public static synchronized RecommendationDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RecommendationDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private RecommendationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String recommendationCreateStatement = "CREATE TABLE " + TABLE_RECOMMENDATIONS +
                "(" +
                KEY_RECOMMENDATION_ID + " INTEGER PRIMARY KEY, " +
                KEY_RECOMMENDATION_USER_ID + " INTEGER REFERENCES " + TABLE_USERS + "," +
                KEY_RECOMMENDATION_NAME + " TEXT, " +
                KEY_RECOMMENDATION_SHORT_DESCRIPTION + " TEXT, " +
                KEY_RECOMMENDATION_IMAGE_URL + " TEXT, " +
                KEY_RECOMMENDATION_LIKED + " INTEGER" +
                ")";
        String userCreateStatement = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY, " +
                KEY_USER_FIRST_NAME + " TEXT," +
                KEY_USER_LAST_NAME + " TEXT" +
                ")";
        db.execSQL(recommendationCreateStatement);
        db.execSQL(userCreateStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    public void addRecommendation(RecommendationModel recommendationModel) {
        SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();
        try {
            long userId = addOrUpdateUser(recommendationModel.getUser());

            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_RECOMMENDATION_USER_ID, userId);
            contentValues.put(KEY_RECOMMENDATION_ID, recommendationModel.getId());
            contentValues.put(KEY_RECOMMENDATION_NAME, recommendationModel.getName());
            contentValues.put(KEY_RECOMMENDATION_SHORT_DESCRIPTION, recommendationModel.getShortDescription());
            contentValues.put(KEY_RECOMMENDATION_IMAGE_URL, recommendationModel.getImageURL());
            Integer liked = recommendationModel.getLiked() ? 1 : 0;
            contentValues.put(KEY_RECOMMENDATION_LIKED, liked);

            database.insertWithOnConflict(TABLE_RECOMMENDATIONS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error occured while saving recommendation", e);
        } finally {
            database.endTransaction();
        }
    }

    public List<RecommendationModel> getRecommendations() {
        List<RecommendationModel> recommendations = new ArrayList<>();

        String query =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s=%s.%s",
                        TABLE_RECOMMENDATIONS,
                        TABLE_USERS,
                        TABLE_RECOMMENDATIONS, KEY_RECOMMENDATION_USER_ID,
                        TABLE_USERS, KEY_USER_ID
                );

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    UserModel userModel =
                            new UserModel()
                                    .setFirstName(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRST_NAME)))
                                    .setLastName(cursor.getString(cursor.getColumnIndex(KEY_USER_LAST_NAME)));
                    RecommendationModel recommendationModel =
                            new RecommendationModel()
                                    .setId(cursor.getInt(cursor.getColumnIndex(KEY_RECOMMENDATION_ID)))
                                    .setImageURL(cursor.getString(cursor.getColumnIndex(KEY_RECOMMENDATION_IMAGE_URL)))
                                    .setUser(userModel)
                                    .setLiked(cursor.getInt(cursor.getColumnIndex(KEY_RECOMMENDATION_LIKED)) != 0)
                                    .setName(cursor.getString(cursor.getColumnIndex(KEY_RECOMMENDATION_NAME)))
                                    .setShortDescription(cursor.getString(cursor.getColumnIndex(KEY_RECOMMENDATION_SHORT_DESCRIPTION)));
                    recommendations.add(recommendationModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error occured while reading posts", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return recommendations;
    }

    public void deleteAllPostsAndUsers() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(TABLE_RECOMMENDATIONS, null, null);
            database.delete(TABLE_USERS, null, null);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error occured while deleting database", e);
        } finally {
            database.endTransaction();
        }
    }


    private long addOrUpdateUser(UserModel user) {
        SQLiteDatabase database = getWritableDatabase();
        long userId = -1;

        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_USER_FIRST_NAME, user.getFirstName());
            contentValues.put(KEY_USER_LAST_NAME, user.getLastName());

            int rows = database.update(TABLE_USERS, contentValues, KEY_USER_FIRST_NAME + "=? AND " + KEY_USER_LAST_NAME + "=?", new String[]{user.getFirstName(), user.getLastName()});

            if (rows == 1) {
                String userSelectedQuery = String.format("SELECT %s FROM %s WHERE %s = ? AND %s = ?", KEY_USER_ID, TABLE_USERS, KEY_USER_FIRST_NAME, KEY_USER_LAST_NAME);
                Cursor cursor = database.rawQuery(userSelectedQuery, new String[]{user.getFirstName(), user.getLastName()});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        database.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                userId = database.insertOrThrow(TABLE_USERS, null, contentValues);
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error occured while creating/updating user", e);
        } finally {
            database.endTransaction();
        }
        return userId;
    }

    public RecommendationModel getRecommendationById(int id) {
        RecommendationModel recommendationModel = null;

        String query =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s=%s.%s WHERE %s.%s = ?",
                        TABLE_RECOMMENDATIONS,
                        TABLE_USERS,
                        TABLE_RECOMMENDATIONS, KEY_RECOMMENDATION_USER_ID,
                        TABLE_USERS, KEY_USER_ID,
                        TABLE_USERS, KEY_USER_ID
                );
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{String.valueOf(id)});

        try {
            if (cursor.moveToFirst()) {
                UserModel userModel =
                        new UserModel()
                                .setFirstName(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRST_NAME)))
                                .setLastName(cursor.getString(cursor.getColumnIndex(KEY_USER_LAST_NAME)));
                recommendationModel =
                        new RecommendationModel()
                                .setId(cursor.getInt(cursor.getColumnIndex(KEY_RECOMMENDATION_ID)))
                                .setImageURL(cursor.getString(cursor.getColumnIndex(KEY_RECOMMENDATION_IMAGE_URL)))
                                .setUser(userModel)
                                .setLiked(cursor.getInt(cursor.getColumnIndex(KEY_RECOMMENDATION_LIKED)) != 0)
                                .setName(cursor.getString(cursor.getColumnIndex(KEY_RECOMMENDATION_NAME)))
                                .setShortDescription(cursor.getString(cursor.getColumnIndex(KEY_RECOMMENDATION_SHORT_DESCRIPTION)));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while fetching database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return recommendationModel;
    }
}

package inc.ahmedmourad.popularmovies.model.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import inc.ahmedmourad.popularmovies.model.database.MovieContract.VideosEntry;
import inc.ahmedmourad.popularmovies.view.controllers.DetailsController;

public class VideosEntity implements Persistable {

    @SerializedName(value = "key")
    public String key = "";

    @SerializedName(value = "name")
    public String name = "";

    @SerializedName(value = "type")
    public String type = "";

    @SerializedName(value = "size")
    public int size = -1;

    @SerializedName(value = "site")
    public String site = "";

    public static VideosEntity fromCursor(final Cursor cursor) {

        final VideosEntity videosEntity = new VideosEntity();

        videosEntity.key = cursor.getString(DetailsController.COL_VIDEOS_KEY);
        videosEntity.name = cursor.getString(DetailsController.COL_VIDEOS_NAME);
        videosEntity.type = cursor.getString(DetailsController.COL_VIDEOS_TYPE);
        videosEntity.size = cursor.getInt(DetailsController.COL_VIDEOS_SIZE);

        return videosEntity;
    }

    public ContentValues toContentValues(final long movieId) {

        final ContentValues contentValues = new ContentValues();

        contentValues.put(VideosEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(VideosEntry.COLUMN_KEY, key);
        contentValues.put(VideosEntry.COLUMN_NAME, name);
        contentValues.put(VideosEntry.COLUMN_TYPE, type);
        contentValues.put(VideosEntry.COLUMN_SIZE, size);

        return contentValues;
    }

}

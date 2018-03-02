package inc.ahmedmourad.popularmovies.model.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import inc.ahmedmourad.popularmovies.model.database.MovieContract.ReviewsEntry;
import inc.ahmedmourad.popularmovies.view.controllers.DetailsController;

public class ReviewsEntity implements Persistable {

    @SerializedName(value = "id")
    public String id = "";

    @SerializedName(value = "author")
    public String author = "";

    @SerializedName(value = "content")
    public String content = "";

    @SerializedName(value = "url")
    public String url = "";

    public static ReviewsEntity fromCursor(final Cursor cursor) {

        final ReviewsEntity reviewsEntity = new ReviewsEntity();

        reviewsEntity.id = cursor.getString(DetailsController.COL_REVIEWS_ID);
        reviewsEntity.author = cursor.getString(DetailsController.COL_REVIEWS_AUTHOR);
        reviewsEntity.content = cursor.getString(DetailsController.COL_REVIEWS_CONTENT);
        reviewsEntity.url = cursor.getString(DetailsController.COL_REVIEWS_URL);

        return reviewsEntity;
    }

    public ContentValues toContentValues(final long movieId) {

        final ContentValues contentValues = new ContentValues();

        contentValues.put(ReviewsEntry.COLUMN_ID, id);
        contentValues.put(ReviewsEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(ReviewsEntry.COLUMN_AUTHOR, author);
        contentValues.put(ReviewsEntry.COLUMN_CONTENT, content);
        contentValues.put(ReviewsEntry.COLUMN_URL, url);

        return contentValues;
    }
}

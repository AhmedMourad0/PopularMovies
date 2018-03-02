package inc.ahmedmourad.popularmovies.model.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import inc.ahmedmourad.popularmovies.model.database.MovieContract.MoviesEntry;
import inc.ahmedmourad.popularmovies.view.controllers.MoviesController;

public class SimpleMoviesEntity implements Persistable {

    @SerializedName("id")
    public long id = -1L;

    @SerializedName("poster_path")
    public String posterPath = "";

    @SerializedName("release_date")
    public String releaseDate = "";

    @SerializedName("original_title")
    public String originalTitle = "";

    @SerializedName("vote_average")
    public double votesAverage = 0.0;

    @SerializedName("adult")
    public boolean isAdult = false;

    @SerializedName("overview")
    public String overview = "";

    public ContentValues toContentValues() {

        final ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesEntry.COLUMN_ID, id);
        contentValues.put(MoviesEntry.COLUMN_POSTER_PATH, posterPath);
        contentValues.put(MoviesEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
        contentValues.put(MoviesEntry.COLUMN_IS_ADULT, Boolean.toString(isAdult));
        contentValues.put(MoviesEntry.COLUMN_VOTES_AVERAGE, votesAverage / 2.0);

        return contentValues;
    }

    public static SimpleMoviesEntity fromCursor(final Cursor cursor) {

        final SimpleMoviesEntity moviesEntity = new SimpleMoviesEntity();

        moviesEntity.id = cursor.getLong(MoviesController.COL_ID);
        moviesEntity.originalTitle = cursor.getString(MoviesController.COL_ORIGINAL_TITLE);
        moviesEntity.posterPath = cursor.getString(MoviesController.COL_POSTER_PATH);
        moviesEntity.overview = cursor.getString(MoviesController.COL_OVERVIEW);
        moviesEntity.votesAverage = cursor.getDouble(MoviesController.COL_VOTES_AVERAGE);
        moviesEntity.releaseDate = cursor.getString(MoviesController.COL_RELEASE_DATE);
        moviesEntity.isAdult = Boolean.valueOf(cursor.getString(MoviesController.COL_IS_ADULT));

        return moviesEntity;
    }
}

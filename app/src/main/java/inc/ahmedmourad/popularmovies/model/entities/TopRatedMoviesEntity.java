package inc.ahmedmourad.popularmovies.model.entities;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import inc.ahmedmourad.popularmovies.model.database.MovieContract;

public class TopRatedMoviesEntity implements Persistable {

    @SuppressWarnings("WeakerAccess")
    @SerializedName("id")
    public long movieId = -1L;

    // For Gson
    public TopRatedMoviesEntity() {

    }

    public TopRatedMoviesEntity(final long movieId) {

        this.movieId = movieId;
    }

    public ContentValues toContentValues() {

        final ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.TopRatedEntry.COLUMN_MOVIE_ID, movieId);

        return contentValues;
    }
}

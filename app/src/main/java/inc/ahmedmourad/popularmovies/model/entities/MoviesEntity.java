package inc.ahmedmourad.popularmovies.model.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.popularmovies.model.database.MovieContract.MoviesEntry;
import inc.ahmedmourad.popularmovies.view.controllers.DetailsController;

public class MoviesEntity implements Persistable {

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

    @SerializedName("backdrop_path")
    public String backdropPath = "";

    @SerializedName("adult")
    public boolean isAdult = false;

    @SerializedName("overview")
    public String overview = "";

    @SerializedName("runtime")
    public int runtime = -1;

    @SerializedName("genres")
    public List<MoviesGenre> genres = new ArrayList<>();

    @SerializedName("tagline")
    public String tagline = "";

    public boolean isFavourite = false;

    public static MoviesEntity fromCursor(final Cursor cursor) {

        final MoviesEntity moviesEntity = new MoviesEntity();

        moviesEntity.id = cursor.getLong(DetailsController.COL_MOVIE_ID);
        moviesEntity.originalTitle = cursor.getString(DetailsController.COL_MOVIE_ORIGINAL_TITLE);
        moviesEntity.posterPath = cursor.getString(DetailsController.COL_MOVIE_POSTER_PATH);
        moviesEntity.overview = cursor.getString(DetailsController.COL_MOVIE_OVERVIEW);
        moviesEntity.votesAverage = cursor.getDouble(DetailsController.COL_MOVIE_VOTES_AVERAGE);
        moviesEntity.releaseDate = cursor.getString(DetailsController.COL_MOVIE_RELEASE_DATE);
        moviesEntity.isAdult = Boolean.valueOf(cursor.getString(DetailsController.COL_MOVIE_IS_ADULT));
        moviesEntity.backdropPath = cursor.getString(DetailsController.COL_MOVIE_BACKDROP_PATH);
        moviesEntity.runtime = cursor.getInt(DetailsController.COL_MOVIE_RUNTIME);
        moviesEntity.genres = stringToList(cursor.getString(DetailsController.COL_MOVIE_GENRES));
        moviesEntity.tagline = cursor.getString(DetailsController.COL_MOVIE_TAGLINE);
        moviesEntity.isFavourite = Boolean.valueOf(cursor.getString(DetailsController.COL_MOVIE_IS_FAVOURITE));

        return moviesEntity;
    }

    public ContentValues toContentValues() {

        final ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesEntry.COLUMN_ID, id);
        contentValues.put(MoviesEntry.COLUMN_POSTER_PATH, posterPath);
        contentValues.put(MoviesEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
        contentValues.put(MoviesEntry.COLUMN_BACKDROP_PATH, backdropPath);
        contentValues.put(MoviesEntry.COLUMN_IS_ADULT, Boolean.toString(isAdult));
        contentValues.put(MoviesEntry.COLUMN_VOTES_AVERAGE, votesAverage / 2.0);
        contentValues.put(MoviesEntry.COLUMN_RUNTIME, runtime);
        contentValues.put(MoviesEntry.COLUMN_GENRES, listToString(genres));
        contentValues.put(MoviesEntry.COLUMN_TAGLINE, tagline);

        return contentValues;
    }

    @NonNull
    private String listToString(final List<MoviesGenre> moviesGenres) {

        return TextUtils.join("||||", moviesGenres);
    }

    @NonNull
    private static List<MoviesGenre> stringToList(final String genres) {

        final List<MoviesGenre> list = new ArrayList<>();

        for (final String genre : genres.split("\\|\\|\\|\\|"))
            list.add(new MoviesGenre(genre));

        return list;
    }
}

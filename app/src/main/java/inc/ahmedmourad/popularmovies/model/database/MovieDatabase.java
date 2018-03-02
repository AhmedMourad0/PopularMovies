package inc.ahmedmourad.popularmovies.model.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.popularmovies.model.database.MovieContract.FavouritesEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.MoviesEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.PopularEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.ReviewsEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.TopRatedEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.VideosEntry;
import inc.ahmedmourad.popularmovies.utils.PreferencesUtils;

public final class MovieDatabase {

    /**
     * checks whether our data are not complete and needs to sync
     *
     * @param context context
     * @return whether we need to sync our data or not
     */
    public static boolean needsSync(final Context context) {

        final Cursor cursor = context.getContentResolver().query(
                MoviesEntry.CONTENT_URI,
                new String[]{MoviesEntry.COLUMN_ID},
                null,
                null,
                null);

        boolean result = true;

        if (cursor != null) {

            // movies tables must have at least 20 records
            result = cursor.getCount() < 20;

            cursor.close();
        }

        return result;
    }

    /**
     * clears everything in our database -except for favourites- for a fresh start
     *
     * @param context context
     */
    public static void reset(final Context context) {

        PreferencesUtils.edit(context, e -> e.putBoolean(PreferencesUtils.KEY_IS_DATA_INITIALIZED, false));

        final ContentResolver contentResolver = context.getContentResolver();

        final String[] args = {"SELECT " + FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_MOVIE_ID + " FROM " + FavouritesEntry.TABLE_NAME};

        contentResolver.delete(MoviesEntry.CONTENT_URI, getWhereClause(MoviesEntry.COLUMN_ID), args);
        contentResolver.delete(PopularEntry.CONTENT_URI, null, null);
        contentResolver.delete(TopRatedEntry.CONTENT_URI, null, null);
        contentResolver.delete(ReviewsEntry.CONTENT_URI, getWhereClause(ReviewsEntry.COLUMN_MOVIE_ID), args);
        contentResolver.delete(VideosEntry.CONTENT_URI, getWhereClause(VideosEntry.COLUMN_MOVIE_ID), args);
    }

    @NonNull
    @Contract(pure = true)
    private static String getWhereClause(final String column) {
        return column + " NOT IN (?)";
    }

    /**
     * clears favourites for a fresh start
     *
     * @param context context
     */
    public static void resetFavourites(final Context context) {

        final ContentResolver contentResolver = context.getContentResolver();

        contentResolver.delete(MoviesEntry.CONTENT_URI, null, null);
        contentResolver.delete(ReviewsEntry.CONTENT_URI, null, null);
        contentResolver.delete(VideosEntry.CONTENT_URI, null, null);
    }

    /**
     * clears favourites for a fresh start
     *
     * @param context context
     */
    public static List<Integer> getFavouritesIds(final Context context) {

        final ContentResolver contentResolver = context.getContentResolver();

        final Cursor cursor = contentResolver
                .query(FavouritesEntry.CONTENT_URI,
                        new String[]{FavouritesEntry.COLUMN_MOVIE_ID},
                        null,
                        null,
                        null);

        if (cursor != null) {

            final List<Integer> ids = new ArrayList<>(cursor.getCount());

            if (cursor.moveToFirst()) {

                do {

                    ids.add(cursor.getInt(0));

                } while (cursor.moveToNext());
            }

            cursor.close();

            return ids;
        }

        return new ArrayList<>(0);
    }

    /**
     * checks if the complete version of this movie is available
     *
     * @param context context
     * @param id      the movie id
     * @return 'true' if the complete version of the movie exists, 'false' otherwise
     */
    public static boolean isMovieAvailable(final Context context, final long id) {

        final Cursor cursor = context.getContentResolver()
                .query(MoviesEntry.buildMovieUriWithId(id),
                        new String[]{MoviesEntry.COLUMN_TAGLINE},
                        null,
                        null,
                        null);

        final boolean cursorIsNull = cursor == null;

        final boolean isAvailable =
                !cursorIsNull && cursor.moveToFirst() && cursor.getString(0) != null;

        if (!cursorIsNull)
            cursor.close();

        return isAvailable;
    }
}

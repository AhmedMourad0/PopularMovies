package inc.ahmedmourad.popularmovies.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import inc.ahmedmourad.popularmovies.model.database.MovieContract.FavouritesEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.MoviesEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.PopularEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.ReviewsEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.TopRatedEntry;
import inc.ahmedmourad.popularmovies.model.database.MovieContract.VideosEntry;

class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "movies.db";

    MovieDbHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE IF NOT EXISTS " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry.COLUMN_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," +
                MoviesEntry.COLUMN_IS_ADULT + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_VOTES_AVERAGE + " REAL NOT NULL," +
                MoviesEntry.COLUMN_POSTER_PATH + " TEXT," +
                MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT," +
                MoviesEntry.COLUMN_RUNTIME + " INTEGER," +
                MoviesEntry.COLUMN_GENRES + " TEXT," +
                MoviesEntry.COLUMN_TAGLINE + " TEXT" +
                ");";

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE IF NOT EXISTS " + FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                " FOREIGN KEY (" + FavouritesEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_ID + ") ON DELETE CASCADE);";

        final String SQL_CREATE_FAVOURITES_INDEX =  "CREATE UNIQUE INDEX IF NOT EXISTS " + FavouritesEntry.INDEX_MOVIE_ID +
                " ON " + FavouritesEntry.TABLE_NAME + "(" + FavouritesEntry.COLUMN_MOVIE_ID + ");";

        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE IF NOT EXISTS " + PopularEntry.TABLE_NAME + " (" +
                PopularEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PopularEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                " FOREIGN KEY (" + PopularEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_ID + ") ON DELETE CASCADE);";

        final String SQL_CREATE_POPULAR_INDEX =  "CREATE UNIQUE INDEX IF NOT EXISTS " + PopularEntry.INDEX_MOVIE_ID +
                " ON " + PopularEntry.TABLE_NAME + "(" + PopularEntry.COLUMN_MOVIE_ID + ");";

        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE IF NOT EXISTS " + TopRatedEntry.TABLE_NAME + " (" +
                TopRatedEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TopRatedEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE," +
                " FOREIGN KEY (" + TopRatedEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_ID + ") ON DELETE CASCADE);";

        final String SQL_CREATE_TOP_RATED_INDEX = "CREATE UNIQUE INDEX IF NOT EXISTS " + TopRatedEntry.INDEX_MOVIE_ID +
                " ON " + TopRatedEntry.TABLE_NAME + "(" + TopRatedEntry.COLUMN_MOVIE_ID + ");";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry.COLUMN_ID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE," +
                ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                ReviewsEntry.COLUMN_URL + " TEXT NOT NULL," +
                " FOREIGN KEY (" + ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_ID + ") ON DELETE CASCADE);";

        final String SQL_CREATE_REVIEWS_INDEX = "CREATE UNIQUE INDEX IF NOT EXISTS " + ReviewsEntry.INDEX_REVIEWS +
                " ON " + ReviewsEntry.TABLE_NAME + "(" + ReviewsEntry.COLUMN_MOVIE_ID + ", " + ReviewsEntry.COLUMN_URL + ");";

        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " + VideosEntry.TABLE_NAME + " (" +
                VideosEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                VideosEntry.COLUMN_KEY + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE," +
                VideosEntry.COLUMN_NAME + " TEXT NOT NULL," +
                VideosEntry.COLUMN_TYPE + " TEXT NOT NULL," +
                VideosEntry.COLUMN_SIZE + " INTEGER NOT NULL," +
                " FOREIGN KEY (" + VideosEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MoviesEntry.TABLE_NAME + " (" + MovieContract.MoviesEntry.COLUMN_ID + ") ON DELETE CASCADE);";

        final String SQL_CREATE_VIDEOS_INDEX = "CREATE UNIQUE INDEX IF NOT EXISTS " + VideosEntry.INDEX_VIDEOS +
                " ON " + VideosEntry.TABLE_NAME + "(" + VideosEntry.COLUMN_MOVIE_ID + ", " + VideosEntry.COLUMN_KEY + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_INDEX);
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_INDEX);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_INDEX);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_INDEX);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_INDEX);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if we change the version number for our database.
        // It does NOT depend on the version number for our application.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP INDEX IF EXISTS " + FavouritesEntry.INDEX_MOVIE_ID);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP INDEX IF EXISTS " + PopularEntry.INDEX_MOVIE_ID);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TopRatedEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP INDEX IF EXISTS " + TopRatedEntry.INDEX_MOVIE_ID);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP INDEX IF EXISTS " + ReviewsEntry.INDEX_REVIEWS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideosEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP INDEX IF EXISTS " + VideosEntry.INDEX_VIDEOS);

        onCreate(sqLiteDatabase);
    }
}

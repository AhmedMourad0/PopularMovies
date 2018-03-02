package inc.ahmedmourad.popularmovies.model.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public final class MovieContract {

    static final String CONTENT_AUTHORITY = "inc.ahmedmourad.popularmovies";

    static final String PATH_MOVIES = "movies";
    static final String PATH_FAVOURITES = "favourites";
    static final String PATH_POPULAR = "popular";
    static final String PATH_TOP_RATED = "top_rated";
    static final String PATH_REVIEWS = "reviews";
    static final String PATH_VIDEOS = "videos";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Don't need count so not implementing BaseColumns
    public static final class MoviesEntry {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_VOTES_AVERAGE = "vote_average";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_IS_ADULT = "is_adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_TAGLINE = "tagline";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        //content://inc.ahmedmourad.popularmovies/movies/#
        public static Uri buildMovieUriWithId(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static long getMovieIdFromUri(final Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    public static final class FavouritesEntry {

        static final String TABLE_NAME = "favourite_movies";

        static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        static final String INDEX_MOVIE_ID = "favourite_movie_id_index";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

        //content://inc.ahmedmourad.popularmovies/favourites/#
        static Uri buildFavouriteMovieUriWithId(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PopularEntry {

        static final String TABLE_NAME = "popular_movies";

        static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        static final String INDEX_MOVIE_ID = "popular_movie_id_index";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        //content://inc.ahmedmourad.popularmovies/popular/#
        static Uri buildPopularMovieUriWithId(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TopRatedEntry {

        static final String TABLE_NAME = "top_rated_movies";

        static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        static final String INDEX_MOVIE_ID = "top_rated_movie_id_index";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED).build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;

        //content://inc.ahmedmourad.popularmovies/top_rated/#
        static Uri buildTopRatedMovieUriWithId(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewsEntry {

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

        static final String PATH_SIMPLE = "simple";

        static final String INDEX_REVIEWS = "index_reviews";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        //content://inc.ahmedmourad.popularmovies/reviews/#
        static Uri buildReviewsUriWithId(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://inc.ahmedmourad.popularmovies/reviews/movie_id/#
        public static Uri buildReviewsUriWithMovieId(final long id) {
            return CONTENT_URI.buildUpon().appendPath(COLUMN_MOVIE_ID).appendPath(Long.toString(id)).build();
        }

        //content://inc.ahmedmourad.popularmovies/reviews/simple/movie_id/#
        public static Uri buildSimpleReviewsUriWithMovieId(final long id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_SIMPLE).appendPath(COLUMN_MOVIE_ID).appendPath(Long.toString(id)).build();
        }

        static long getMovieIdFromUri(final Uri uri) {
            return Long.parseLong(uri.getLastPathSegment());
        }
    }

    public static final class VideosEntry {

        static final String TABLE_NAME = "videos";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SIZE = "size";

        static final String INDEX_VIDEOS = "index_videos";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;

        //content://inc.ahmedmourad.popularmovies/videos/#
        static Uri buildVideosUriWithId(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://inc.ahmedmourad.popularmovies/videos/movie_id/#
        public static Uri buildVideosUriWithMovieId(final long id) {
            return CONTENT_URI.buildUpon().appendPath(COLUMN_MOVIE_ID).appendPath(Long.toString(id)).build();
        }

        static long getMovieIdFromVideosUri(final Uri uri) {
            return Long.parseLong(uri.getLastPathSegment());
        }
    }
}

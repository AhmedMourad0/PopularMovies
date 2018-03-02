package inc.ahmedmourad.popularmovies.model.sync;

import android.content.Context;

import java.util.List;

import inc.ahmedmourad.popularmovies.model.api.ApiClient;
import inc.ahmedmourad.popularmovies.model.api.ApiInterface;
import inc.ahmedmourad.popularmovies.model.database.MovieDatabase;
import inc.ahmedmourad.popularmovies.utils.NetworkUtils;
import inc.ahmedmourad.popularmovies.utils.PreferencesUtils;

final class SyncTask {

    /**
     * Our data is getting real old, time to refresh them
     * @param context context, obviously
     */
    static synchronized void sync(final Context context) {

        final ApiInterface client = ApiClient.getInstance().create(ApiInterface.class);

        MovieDatabase.reset(context);

        fetchMoviesData(context, client);

        PreferencesUtils.edit(context, e -> e.putBoolean(PreferencesUtils.KEY_IS_DATA_INITIALIZED, true));
    }

    /**
     * Fetch our data from the Api and cache them in our database
     * @param context Context, Context everywhere!
     * @param client our client used for fetching data, pass null if you're too lazy to create one
     */
    private static void fetchMoviesData(final Context context, final ApiInterface client) {

        NetworkUtils.fetchPopularMoviesData(context, client);

        NetworkUtils.fetchTopRatedMoviesData(context, client);
    }

    /**
     * Our data is getting real old, time to refresh them
     * @param context context, obviously
     */
    static synchronized void syncFavourites(final Context context) {

        final List<Integer> ids = MovieDatabase.getFavouritesIds(context);

        MovieDatabase.resetFavourites(context);

        for (final int id : ids)
            NetworkUtils.fetchSingleMovieData(context, id);
    }
}

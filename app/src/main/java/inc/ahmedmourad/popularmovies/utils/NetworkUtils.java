package inc.ahmedmourad.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.model.api.ApiClient;
import inc.ahmedmourad.popularmovies.model.api.ApiInterface;
import inc.ahmedmourad.popularmovies.model.database.MovieContract;
import inc.ahmedmourad.popularmovies.model.entities.Persistable;
import io.reactivex.schedulers.Schedulers;

import static inc.ahmedmourad.popularmovies.utils.ConcurrencyUtils.runOnUiThread;

public final class NetworkUtils {

    /**
     * Fetch complete data for a single movie
     *
     * @param context The secret key
     * @param id      id
     */
    @SuppressWarnings("expression")
    public static void fetchSingleMovieData(final Context context, final long id) {

        final ApiInterface client = ApiClient.getInstance().create(ApiInterface.class);

        client.getMovie(id)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap(movieResponse -> {

                    context.getContentResolver()
                            .insert(MovieContract.MoviesEntry.CONTENT_URI,
                                    movieResponse.toContentValues());

                    return client.getMovieVideos(id);

                })
                .flatMap(videosResponse -> {

                    context.getContentResolver()
                            .bulkInsert(MovieContract.VideosEntry.CONTENT_URI,
                                    listToContentProviderArray(videosResponse.getVideos(), id));

                    return client.getMovieReviews(id);

                })
                .subscribe(reviewsResponse -> context.getContentResolver()
                        .bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI,
                                listToContentProviderArray(reviewsResponse.reviews, id)), throwable -> {

                    // static import, because it's pretty
                    runOnUiThread(context, () -> {

                        if (throwable == null ||
                                throwable instanceof ConnectException ||
                                throwable instanceof UnknownHostException)
                            Toast.makeText(
                                    context,
                                    R.string.network_error,
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.network_error_cause, throwable.getLocalizedMessage(), throwable.getCause().getLocalizedMessage()),
                                    Toast.LENGTH_LONG).show();
                    });

                    throwable.printStackTrace();

                });
    }

    /**
     * Fetch the 20 popular movies of the first page
     *
     * @param context Batman's Belt
     * @param client  client
     */
    public static void fetchPopularMoviesData(final Context context, ApiInterface client) {

        if (client == null)
            client = ApiClient.getInstance().create(ApiInterface.class);

        client.getPopularMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {

                    if (response != null) {

                        context.getContentResolver().bulkInsert(MovieContract.MoviesEntry.CONTENT_URI, listToContentProviderArray(response.movies, -1L));
                        context.getContentResolver().bulkInsert(MovieContract.PopularEntry.CONTENT_URI, listToContentProviderArray(response.getPopularMovies(), -1L));
                    }


                }, throwable -> {

                    // static import
                    runOnUiThread(context, () -> {

                        if (throwable == null ||
                                throwable instanceof ConnectException ||
                                throwable instanceof UnknownHostException)
                            Toast.makeText(
                                    context,
                                    R.string.network_error,
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.network_error_cause, throwable.getLocalizedMessage(), throwable.getCause().getLocalizedMessage()),
                                    Toast.LENGTH_LONG).show();
                    });

                    throwable.printStackTrace();
                });
    }

    /**
     * Fetch the 20 top rated movies of the first page
     *
     * @param context Your secret identity
     * @param client  client
     */
    public static void fetchTopRatedMoviesData(final Context context, ApiInterface client) {

        if (client == null)
            client = ApiClient.getInstance().create(ApiInterface.class);

        client.getTopRatedMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {

                    if (response != null) {

                        context.getContentResolver().bulkInsert(MovieContract.MoviesEntry.CONTENT_URI, listToContentProviderArray(response.movies, -1L));
                        context.getContentResolver().bulkInsert(MovieContract.TopRatedEntry.CONTENT_URI, listToContentProviderArray(response.getTopRatedMovies(), -1L));
                    }

                }, throwable -> {

                    // static import
                    runOnUiThread(context, () -> {

                        if (throwable == null ||
                                throwable instanceof ConnectException ||
                                throwable instanceof UnknownHostException)
                            Toast.makeText(
                                    context,
                                    R.string.network_error,
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.network_error_cause, throwable.getLocalizedMessage(), throwable.getCause().getLocalizedMessage()),
                                    Toast.LENGTH_LONG).show();
                    });

                    throwable.printStackTrace();
                });
    }

    private static <T extends Persistable> ContentValues[] listToContentProviderArray(final List<T> list, final long movieId) {

        final ContentValues[] contentValues = new ContentValues[list.size()];

        if (movieId == -1) {

            for (int i = 0; i < list.size(); ++i)
                contentValues[i] = list.get(i).toContentValues();

        } else {

            for (int i = 0; i < list.size(); ++i)
                contentValues[i] = list.get(i).toContentValues(movieId);
        }

        return contentValues;
    }
}

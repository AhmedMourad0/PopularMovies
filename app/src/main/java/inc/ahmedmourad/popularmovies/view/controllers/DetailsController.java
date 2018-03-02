package inc.ahmedmourad.popularmovies.view.controllers;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.SubtitleCollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.popularmovies.BuildConfig;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.adapters.ReviewsRecyclerAdapter;
import inc.ahmedmourad.popularmovies.adapters.VideosRecyclerAdapter;
import inc.ahmedmourad.popularmovies.model.database.MovieContract;
import inc.ahmedmourad.popularmovies.model.database.MovieDatabase;
import inc.ahmedmourad.popularmovies.model.entities.MoviesEntity;
import inc.ahmedmourad.popularmovies.model.entities.MoviesGenre;
import inc.ahmedmourad.popularmovies.model.entities.ReviewsEntity;
import inc.ahmedmourad.popularmovies.model.entities.VideosEntity;
import inc.ahmedmourad.popularmovies.utils.NetworkUtils;
import inc.ahmedmourad.popularmovies.utils.PreferencesUtils;
import inc.ahmedmourad.popularmovies.view.activity.MainActivity;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * This's where we display the details of our movie
 */
public class DetailsController extends Controller implements AppBarLayout.OnOffsetChangedListener, VideosRecyclerAdapter.OnClickListener, ReviewsRecyclerAdapter.OnFooterClickListener, ReviewsRecyclerAdapter.OnLongClickListener {

    static final String KEY_ID = "id";

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_ORIGINAL_TITLE = 1;
    public static final int COL_MOVIE_POSTER_PATH = 2;
    public static final int COL_MOVIE_OVERVIEW = 3;
    public static final int COL_MOVIE_VOTES_AVERAGE = 4;
    public static final int COL_MOVIE_RELEASE_DATE = 5;
    public static final int COL_MOVIE_IS_ADULT = 6;
    public static final int COL_MOVIE_RUNTIME = 7;
    public static final int COL_MOVIE_BACKDROP_PATH = 8;
    public static final int COL_MOVIE_GENRES = 9;
    public static final int COL_MOVIE_TAGLINE = 10;
    public static final int COL_MOVIE_IS_FAVOURITE = 11;

    private static String[] COLUMNS_MOVIE;

    public static final int COL_REVIEWS_ID = 0;
    public static final int COL_REVIEWS_AUTHOR = 1;
    public static final int COL_REVIEWS_CONTENT = 2;
    public static final int COL_REVIEWS_URL = 3;

    static final String[] COLUMNS_REVIEWS = new String[]{
            MovieContract.ReviewsEntry.TABLE_NAME + "." + MovieContract.ReviewsEntry.COLUMN_ID,
            MovieContract.ReviewsEntry.COLUMN_AUTHOR,
            MovieContract.ReviewsEntry.COLUMN_CONTENT,
            MovieContract.ReviewsEntry.COLUMN_URL,
    };

    public static final int COL_VIDEOS_KEY = 0;
    public static final int COL_VIDEOS_NAME = 1;
    public static final int COL_VIDEOS_TYPE = 2;
    public static final int COL_VIDEOS_SIZE = 3;

    private static final String[] COLUMNS_VIDEOS = new String[]{
            MovieContract.VideosEntry.COLUMN_KEY,
            MovieContract.VideosEntry.COLUMN_NAME,
            MovieContract.VideosEntry.COLUMN_TYPE,
            MovieContract.VideosEntry.COLUMN_SIZE
    };

    @BindView(R.id.details_overview)
    TextView overviewTextView;

    @BindView(R.id.details_runtime)
    TextView runtimeTextView;

    @BindView(R.id.details_rating)
    TextView ratingTextView;

    @BindView(R.id.details_rating_bar)
    MaterialRatingBar ratingBar;

    @BindView(R.id.details_poster)
    ImageView posterImageView;

    @BindView(R.id.details_backdrop)
    ImageView backdropImageView;

    @BindView(R.id.details_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.details_collapsing_toolbar)
    SubtitleCollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.details_date)
    TextView dateTextView;

    @BindView(R.id.details_genres)
    FlexboxLayout flexboxLayout;

    @BindView(R.id.details_adult)
    FrameLayout adultFrameLayout;

    @BindView(R.id.details_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.details_videos)
    RecyclerView videosRecyclerView;

    @BindView(R.id.details_reviews)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.details_favourite)
    FloatingActionButton favouriteFab;

    @BindView(R.id.details_switch)
    SwitchCompat playInAppSwitch;

    @BindView(R.id.details_available_reviews)
    TextView noReviewsTextView;

    @BindView(R.id.details_available_videos)
    TextView noVideosTextView;

    @BindView(R.id.details_available_genres)
    TextView noGenresTextView;

    private long id;

    private final List<VideosEntity> videosList = new ArrayList<>();
    private final List<ReviewsEntity> reviewsList = new ArrayList<>();

    private VideosRecyclerAdapter videosRecyclerAdapter;
    private ReviewsRecyclerAdapter reviewsRecyclerAdapter;

    private ContentObserver movieContentObserver;
    private ContentObserver videosContentObserver;
    private ContentObserver reviewsContentObserver;
    private ContentObserver favouritesContentObserver;

    private Context context;

    private boolean isFavourite = false;

    private MoviesEntity movie;

    private Unbinder unbinder;

    @SuppressWarnings("WeakerAccess")
    public DetailsController(@Nullable final Bundle args) {
        super(args);

        if (args != null)
            id = args.getLong(KEY_ID);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup container) {

        final View view = inflater.inflate(R.layout.controller_details, container, false);

        unbinder = ButterKnife.bind(this, view);

        context = view.getContext();

        if (getActivity() != null)
            ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        COLUMNS_MOVIE = new String[]{
                MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_ID,
                MovieContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
                MovieContract.MoviesEntry.COLUMN_POSTER_PATH,
                MovieContract.MoviesEntry.COLUMN_OVERVIEW,
                MovieContract.MoviesEntry.COLUMN_VOTES_AVERAGE,
                MovieContract.MoviesEntry.COLUMN_RELEASE_DATE,
                MovieContract.MoviesEntry.COLUMN_IS_ADULT,
                MovieContract.MoviesEntry.COLUMN_RUNTIME,
                MovieContract.MoviesEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MoviesEntry.COLUMN_GENRES,
                MovieContract.MoviesEntry.COLUMN_TAGLINE,
                getFavouriteColumn()
        };

        initializeVideosRecyclerView();
        initializeReviewsRecyclerView();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(v -> getRouter().popController(this));

        favouriteFab.setOnClickListener(v -> {

            if (isFavourite) {

                context.getContentResolver()
                        .delete(MovieContract.FavouritesEntry.CONTENT_URI,
                                MovieContract.FavouritesEntry.COLUMN_MOVIE_ID + " = ?",
                                new String[]{Long.toString(id)});

            } else {

                final ContentValues contentValues = new ContentValues();

                contentValues.put(MovieContract.FavouritesEntry.COLUMN_MOVIE_ID, id);

                context.getContentResolver().insert(MovieContract.FavouritesEntry.CONTENT_URI, contentValues);
            }

            isFavourite = !isFavourite;

            updateFabIcon();
        });

        movieContentObserver = new ContentObserver(new Handler(Looper.myLooper())) {
            @Override
            public void onChange(final boolean selfChange) {

                initializeMovieData(context);
            }
        };

        videosContentObserver = new ContentObserver(new Handler(Looper.myLooper())) {
            @Override
            public void onChange(final boolean selfChange) {

                displayVideosData(context);
            }
        };

        reviewsContentObserver = new ContentObserver(new Handler(Looper.myLooper())) {
            @Override
            public void onChange(final boolean selfChange) {

                displayReviewsData(context);
            }
        };

        favouritesContentObserver = new ContentObserver(new Handler(Looper.myLooper())) {
            @Override
            public void onChange(final boolean selfChange) {

                COLUMNS_MOVIE[COL_MOVIE_IS_FAVOURITE] = getFavouriteColumn();

                initializeMovieData(context);
            }
        };

        movieContentObserver.onChange(false);
        videosContentObserver.onChange(false);
        reviewsContentObserver.onChange(false);

        playInAppSwitch.setChecked(PreferencesUtils.defaultPrefs(context).getBoolean(PreferencesUtils.KEY_PLAY_VIDEOS_IN_APP, true));

        playInAppSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> PreferencesUtils.edit(context, e -> e.putBoolean(PreferencesUtils.KEY_PLAY_VIDEOS_IN_APP, isChecked)));

        initializeRefreshLayout();

        setRetainViewMode(RetainViewMode.RETAIN_DETACH);

        return view;
    }

    @NonNull
    private String getFavouriteColumn() {

        return "CASE WHEN " + MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_ID +
                " IN (" + TextUtils.join(", ", MovieDatabase.getFavouritesIds(context)) +
                ") THEN 'true' " +
                "ELSE 'false' " +
                "END AS is_favourite";
    }

    private void initializeVideosRecyclerView() {

        videosRecyclerAdapter = new VideosRecyclerAdapter(videosList, this);
        videosRecyclerView.setAdapter(videosRecyclerAdapter);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        videosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videosRecyclerView.setHorizontalScrollBarEnabled(true);
        videosRecyclerView.setHasFixedSize(true);
    }

    private void initializeReviewsRecyclerView() {

        reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviewsList, this, this);
        reviewsRecyclerView.setAdapter(reviewsRecyclerAdapter);
        reviewsRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setVerticalScrollBarEnabled(true);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * initialize our refreshLayout
     */
    private void initializeRefreshLayout() {

        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.refresh_progress_background);

        refreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        refreshLayout.setOnRefreshListener(() -> {
            NetworkUtils.fetchSingleMovieData(context, id);
            refreshLayout.setRefreshing(false);
        });
    }

    /**
     * if complete movie is in the database display it, otherwise fetch it from the Api
     *
     * @param context c for short
     */
    private void initializeMovieData(final Context context) {

        if (MovieDatabase.isMovieAvailable(context, id))
            displayMovieData(context);
        else
            NetworkUtils.fetchSingleMovieData(context, id);
    }

    /**
     * populate our UI with movie data from the database
     *
     * @param context stub
     */
    private void displayMovieData(final Context context) {

        final Cursor cursor = context.getContentResolver()
                .query(MovieContract.MoviesEntry.buildMovieUriWithId(id),
                        COLUMNS_MOVIE,
                        null,
                        null,
                        null);

        if (cursor != null)
            displayMovie(context, cursor);
        else
            closeOnError(context);
    }

    /**
     * populate our UI with videos from the database
     *
     * @param context stub
     */
    private void displayVideosData(final Context context) {

       final Cursor cursor = context.getContentResolver()
                .query(MovieContract.VideosEntry.buildVideosUriWithMovieId(id),
                        COLUMNS_VIDEOS,
                        null,
                        null,
                        null);

        if (cursor != null)
            displayVideos(cursor);
        else
            closeOnError(context);
    }

    /**
     * populate our UI with reviews from the database
     *
     * @param context stub
     */
    private void displayReviewsData(final Context context) {

        final Cursor cursor = context.getContentResolver()
                .query(MovieContract.ReviewsEntry.buildSimpleReviewsUriWithMovieId(id),
                        COLUMNS_REVIEWS,
                        null,
                        null,
                        null);

        if (cursor != null)
            displayReviews(cursor);
        else
            closeOnError(context);
    }

    private void displayMovie(final Context context, final Cursor cursor) {

        if (cursor.moveToFirst()) {

            movie = MoviesEntity.fromCursor(cursor);

            cursor.close();

//            collapsingToolbar.setTitleEnabled(false);
//            collapsingToolbar.setSubtitle(movie.tagline);
            collapsingToolbar.setTitle(movie.originalTitle);
            collapsingToolbar.setSubtitle(movie.tagline);

            final String posterBaseUrl = "http://image.tmdb.org/t/p/";

            final Uri posterUri = Uri.parse(posterBaseUrl)
                    .buildUpon()
                    .appendEncodedPath("w342") //"w92", "w154", "w185", "w342", "w500", "w780"
                    .appendEncodedPath(movie.posterPath)
                    .build();

            final Uri backdropUri = Uri.parse(posterBaseUrl)
                    .buildUpon()
                    .appendEncodedPath("w780") //"w92", "w154", "w185", "w342", "w500", "w780"
                    .appendEncodedPath(movie.backdropPath)
                    .build();

            Picasso.with(context)
                    .load(posterUri)
                    .placeholder(R.drawable.placeholder_poster)
                    .error(R.drawable.error_poster)
                    .into(posterImageView);

            Picasso.with(context)
                    .load(backdropUri)
                    .placeholder(R.drawable.placeholder_backdrop)
                    .error(R.drawable.error_backdrop)
                    .into(backdropImageView);

            isFavourite = movie.isFavourite;

            updateFabIcon();

            String date = "";

            try {

                date = DateFormat.format("MMM yyyy",
                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .parse(movie.releaseDate)
                ).toString();

            } catch (final ParseException e) {

                date = date.substring(0, 4);

                e.printStackTrace();
            }

            dateTextView.setText(date);

            final String runtime;

            final int hours = movie.runtime / 60, minutes = movie.runtime % 60;

            if (movie.runtime % 60 == 0)
                runtime = context.getResources().getQuantityString(R.plurals.runtime_hours, hours, hours);
            else if (movie.runtime < 60)
                runtime = context.getResources().getQuantityString(R.plurals.runtime_minutes, minutes, minutes);
            else
                runtime = context.getResources().getQuantityString(R.plurals.runtime_hours, hours, hours) + "  " +
                        context.getResources().getQuantityString(R.plurals.runtime_minutes, minutes, minutes);

            runtimeTextView.setText(runtime);

            overviewTextView.setText(movie.overview);
            ratingTextView.setText(context.getString(R.string.rating, Double.toString(movie.votesAverage).substring(0, 3)));

            ratingBar.setRating((float) movie.votesAverage);

            if (movie.isAdult)
                adultFrameLayout.setVisibility(View.VISIBLE);
            else
                adultFrameLayout.setVisibility(View.GONE);

            displayGenres(context, movie.genres);

        } else {

            cursor.close();
        }
    }

    private void updateFabIcon() {

        if (isFavourite)
            favouriteFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourite));
        else
            favouriteFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourite_border));
    }

    private void displayGenres(final Context context, final List<MoviesGenre> genres) {

        flexboxLayout.removeAllViews();

        if (genres.isEmpty())
            noGenresTextView.setVisibility(View.VISIBLE);
        else
            noGenresTextView.setVisibility(View.GONE);

        View view;

        TextView genreTextView;

        for (final MoviesGenre genre : genres) {

            view = LayoutInflater.from(context).inflate(R.layout.item_genre, flexboxLayout, false);

            genreTextView = view.findViewById(R.id.genre_name);

            genreTextView.setText(genre.name);

            flexboxLayout.addView(view);
        }
    }

    private void displayVideos(final Cursor cursor) {

        if (cursor.moveToFirst()) {

            noVideosTextView.setVisibility(View.GONE);

            videosList.clear();

            do {

                videosList.add(VideosEntity.fromCursor(cursor));

            } while (cursor.moveToNext());

            cursor.close();

            videosRecyclerAdapter.notifyDataSetChanged();

        } else {

            noVideosTextView.setVisibility(View.VISIBLE);

            cursor.close();
        }
    }

    private void displayReviews(final Cursor cursor) {

        if (cursor.moveToFirst()) {

            noReviewsTextView.setVisibility(View.GONE);

            reviewsList.clear();

            do {

                reviewsList.add(ReviewsEntity.fromCursor(cursor));

            } while (cursor.moveToNext());

            cursor.close();

            reviewsRecyclerAdapter.notifyDataSetChanged();

        } else {

            noReviewsTextView.setVisibility(View.VISIBLE);

            cursor.close();
        }
    }

    /**
     * Not exiting the app, just going back to our lists
     *
     * @param context bla
     */
    private void closeOnError(final Context context) {

        getRouter().popController(this);

        Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        if (item.getItemId() == R.id.action_share) {

            final Intent sendIntent = new Intent();

            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");

            String trailerUrl = "";

            if (videosList.size() > 0)
                trailerUrl = "http://www.youtube.com/watch?v=" + videosList.get(0).name;

            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    movie.originalTitle + "\n\n" +
                            movie.overview + "\n\n" +
                            trailerUrl + "\n\n" +
                            "Shared via " + context.getString(R.string.app_name));

            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
        refreshLayout.setEnabled(verticalOffset == 0);
    }

    @Override
    protected void onAttach(@NonNull final View view) {
        super.onAttach(view);

        view.getContext().getContentResolver().registerContentObserver(MovieContract.MoviesEntry.CONTENT_URI, true, movieContentObserver);
        view.getContext().getContentResolver().registerContentObserver(MovieContract.VideosEntry.CONTENT_URI, true, videosContentObserver);
        view.getContext().getContentResolver().registerContentObserver(MovieContract.ReviewsEntry.CONTENT_URI, true, reviewsContentObserver);
        view.getContext().getContentResolver().registerContentObserver(MovieContract.FavouritesEntry.CONTENT_URI, true, favouritesContentObserver);

        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onDetach(@NonNull final View view) {

        view.getContext().getContentResolver().unregisterContentObserver(movieContentObserver);
        view.getContext().getContentResolver().unregisterContentObserver(videosContentObserver);
        view.getContext().getContentResolver().unregisterContentObserver(reviewsContentObserver);
        view.getContext().getContentResolver().unregisterContentObserver(favouritesContentObserver);

        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onVideoClicked(final VideosEntity video) {

        if (PreferencesUtils.defaultPrefs(context).getBoolean(PreferencesUtils.KEY_PLAY_VIDEOS_IN_APP, true)) {

            if (getActivity() != null) {

                final Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), BuildConfig.YOUTUBE_API_KEY, video.key);
                startActivity(intent);
            }

        } else {

            // Taken from the following SO answer: https://stackoverflow.com/a/12439378/7411799

            try {

                final Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.key));

                context.startActivity(appIntent);

            } catch (final ActivityNotFoundException ex) {

                final Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.key));

                context.startActivity(webIntent);
            }
        }
    }

    @Override
    public void onReviewFooterClicked() {

        final Bundle bundle = new Bundle();

        bundle.putString(ReviewsController.KEY_TITLE, movie.originalTitle + " Reviews");
        bundle.putLong(ReviewsController.KEY_MOVIE_ID, id);

        getRouter().pushController(RouterTransaction.with(new ReviewsController(bundle)));
    }

    @Override
    public void onReviewLongClicked(final ReviewsEntity review) {

        final Bundle bundle = new Bundle();

        bundle.putString(BrowserController.KEY_TITLE, movie.originalTitle + " Reviews");
        bundle.putString(BrowserController.KEY_URL, review.url);

        getRouter().pushController(RouterTransaction.with(new BrowserController(bundle)));
    }
}

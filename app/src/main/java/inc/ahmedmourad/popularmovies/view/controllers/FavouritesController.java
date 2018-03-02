package inc.ahmedmourad.popularmovies.view.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.adapters.MoviesRecyclerAdapter;
import inc.ahmedmourad.popularmovies.model.database.MovieContract;
import inc.ahmedmourad.popularmovies.model.entities.SimpleMoviesEntity;
import inc.ahmedmourad.popularmovies.utils.PreferencesUtils;

public class FavouritesController extends Controller implements MoviesRecyclerAdapter.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private ContentObserver moviesObserver;

    private final List<SimpleMoviesEntity> moviesList = new ArrayList<>();

    @BindView(R.id.favourites_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.favourites_available)
    TextView noDataAvailableTextView;

    private MoviesRecyclerAdapter recyclerAdapter;

    private SharedPreferences prefs;

    private Context context;

    private MainController parentController;

    private int item;

    private Unbinder unbinder;

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup container) {

        final View view = inflater.inflate(R.layout.controller_favourites, container, false);

        setHasOptionsMenu(true);

        unbinder = ButterKnife.bind(this, view);

        context = view.getContext();

        prefs = PreferencesUtils.defaultPrefs(context);

        parentController = (MainController) getParentController();

        item = prefs.getInt(PreferencesUtils.KEY_ITEM, PreferencesUtils.ITEM_GRID);

        initializeRecyclerView();

        moviesObserver = new ContentObserver(new Handler(Looper.myLooper())) {
            @Override
            public void onChange(final boolean selfChange) {

                final Cursor cursor = context.getContentResolver()
                        .query(MovieContract.FavouritesEntry.CONTENT_URI,
                                MoviesController.COLUMNS,
                                null,
                                null,
                                null);

                if (cursor != null) {

                    moviesList.clear();

                    if (cursor.moveToFirst()) {

                        noDataAvailableTextView.setVisibility(View.GONE);

                        do {

                            moviesList.add(SimpleMoviesEntity.fromCursor(cursor));

                        } while (cursor.moveToNext());

                    } else {

                        noDataAvailableTextView.setVisibility(View.VISIBLE);
                    }

                    cursor.close();

                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        };

        moviesObserver.onChange(false);

        return view;
    }

    /**
     * initialize our recyclerView
     */
    private void initializeRecyclerView() {

        if (item == PreferencesUtils.ITEM_GRID)
            useGridLayoutManager();
        else
            useLinearLayoutManager();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setHasFixedSize(true);
    }

    private void useLinearLayoutManager() {

        recyclerAdapter = new MoviesRecyclerAdapter(moviesList, this, R.layout.item_movie_linear);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void useGridLayoutManager() {

        recyclerAdapter = new MoviesRecyclerAdapter(moviesList, this, R.layout.item_movie_grid);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onAttach(@NonNull final View view) {
        super.onAttach(view);

        context.getContentResolver().registerContentObserver(MovieContract.FavouritesEntry.CONTENT_URI, true, moviesObserver);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDetach(@NonNull final View view) {

        context.getContentResolver().unregisterContentObserver(moviesObserver);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        if (item.getItemId() == R.id.action_layout)
            swapLayout(item);

        return true;
    }

    private void swapLayout(@NonNull final MenuItem item) {

        final Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        if (prefs.getInt(PreferencesUtils.KEY_ITEM, PreferencesUtils.ITEM_GRID) == PreferencesUtils.ITEM_GRID) {

            useLinearLayoutManager();

            PreferencesUtils.edit(context, e -> e.putInt(PreferencesUtils.KEY_ITEM, PreferencesUtils.ITEM_LINEAR));

            item.setIcon(R.drawable.list_grid);

        } else {

            useGridLayoutManager();

            PreferencesUtils.edit(context, e -> e.putInt(PreferencesUtils.KEY_ITEM, PreferencesUtils.ITEM_GRID));

            item.setIcon(R.drawable.list_linear);
        }

        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    @Override
    public void onMovieClicked(final SimpleMoviesEntity movie) {

        final Bundle bundle = new Bundle();
        bundle.putLong(DetailsController.KEY_ID, movie.id);

        if (parentController != null)
            parentController.getRouter().pushController(RouterTransaction.with(new DetailsController(bundle)));
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {

        if (key.equals(PreferencesUtils.KEY_ITEM)) {

            if (item != sharedPreferences.getInt(PreferencesUtils.KEY_ITEM, PreferencesUtils.ITEM_GRID)) {

                item = sharedPreferences.getInt(PreferencesUtils.KEY_ITEM, PreferencesUtils.ITEM_GRID);

                final Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                if (item == PreferencesUtils.ITEM_GRID)
                    useGridLayoutManager();
                else
                    useLinearLayoutManager();

                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}

package inc.ahmedmourad.popularmovies.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;

import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.view.controllers.FavouritesController;
import inc.ahmedmourad.popularmovies.view.controllers.MoviesController;

public class ConductorPagerAdapter extends RouterPagerAdapter {

    private final Controller host;

    private final Controller[] controllers;

    private final String[] controllersNames;

    public ConductorPagerAdapter(final Controller host) {
        super(host);

        this.host = host;

        final Bundle popularBundle = new Bundle();
        popularBundle.putInt(MoviesController.KEY_MODE, MoviesController.MODE_POPULAR);

        final Bundle topRatedBundle = new Bundle();
        topRatedBundle.putInt(MoviesController.KEY_MODE, MoviesController.MODE_TOP_RATED);

        this.controllers = new Controller[]{
                new MoviesController(popularBundle),
                new MoviesController(topRatedBundle),
                new FavouritesController()
        };

        this.controllersNames = getControllersNames();
    }

    @NonNull
    private String[] getControllersNames() {

        if (host == null)
            throw new IllegalStateException("Host is null");

        if (host.getActivity() == null)
            throw new IllegalStateException("Activity is null");

        return new String[]{
                host.getActivity().getString(R.string.popular_movies),
                host.getActivity().getString(R.string.top_rated_movies),
                host.getActivity().getString(R.string.favourite_movies)
        };
    }

    @Override
    public void configureRouter(@NonNull final Router router, final int position) {

        if (!router.hasRootController())
            router.setRoot(RouterTransaction.with(controllers[position]));
    }

    @Override
    public String getPageTitle(final int position) {

        return controllersNames[position];
    }

    @Override
    public int getCount() {

        return controllers.length;
    }
}

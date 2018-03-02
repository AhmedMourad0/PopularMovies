package inc.ahmedmourad.popularmovies.model.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.popularmovies.model.entities.SimpleMoviesEntity;
import inc.ahmedmourad.popularmovies.model.entities.TopRatedMoviesEntity;

public class TopRatedMoviesResponse {

    private transient final List<TopRatedMoviesEntity> topRatedMovies = new ArrayList<>();

    @SerializedName("results")
    public List<SimpleMoviesEntity> movies;

    public List<TopRatedMoviesEntity> getTopRatedMovies() {

        updateTopRatedMovies();

        return topRatedMovies;
    }

    private void updateTopRatedMovies() {

        topRatedMovies.clear();

        for (final SimpleMoviesEntity it : movies)
            topRatedMovies.add(new TopRatedMoviesEntity(it.id));
    }
}

package inc.ahmedmourad.popularmovies.model.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import inc.ahmedmourad.popularmovies.model.entities.PopularMoviesEntity;
import inc.ahmedmourad.popularmovies.model.entities.SimpleMoviesEntity;

public class PopularMoviesResponse {

    private transient final List<PopularMoviesEntity> popularMovies = new ArrayList<>();

    @SerializedName("results")
    public List<SimpleMoviesEntity> movies;

    public List<PopularMoviesEntity> getPopularMovies() {

        updatePopularMovies();

        return popularMovies;
    }

    private void updatePopularMovies() {

        popularMovies.clear();

        for (final SimpleMoviesEntity it : movies)
            popularMovies.add(new PopularMoviesEntity(it.id));
    }
}

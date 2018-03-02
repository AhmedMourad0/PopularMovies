package inc.ahmedmourad.popularmovies.adapters;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.model.entities.SimpleMoviesEntity;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    private final List<SimpleMoviesEntity> moviesList;
    private final OnClickListener onClickListener;
    private final int itemId;

    public MoviesRecyclerAdapter(final List<SimpleMoviesEntity> moviesList, final OnClickListener onClickListener, @LayoutRes final int itemId) {
        this.moviesList = moviesList;
        this.onClickListener = onClickListener;
        this.itemId = itemId;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup container, final int viewType) {
        return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(itemId, container, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(moviesList.get(position), itemId, onClickListener);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }



    @FunctionalInterface
    public interface OnClickListener {
        void onMovieClicked(final SimpleMoviesEntity movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster)
        ImageView poster;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.rating)
        MaterialRatingBar rating;

        @Nullable
        @BindView(R.id.overview)
        TextView overview;

        @Nullable
        @BindView(R.id.adult)
        TextView adult;

        @Nullable
        @BindView(R.id.year)
        TextView year;

        ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(final SimpleMoviesEntity movie, @LayoutRes final int itemId, final OnClickListener onClickListener) {

            final String posterBaseUrl = "http://image.tmdb.org/t/p/";
            final String pathSize = "w342"; //"w92", "w154", "w185", "w342", "w500", "w780"

            final Uri uri = Uri.parse(posterBaseUrl).buildUpon()
                    .appendEncodedPath(pathSize)
                    .appendEncodedPath(movie.posterPath)
                    .build();

            Picasso.with(itemView.getContext())
                    .load(uri.toString())
                    .placeholder(R.drawable.placeholder_poster)
                    .error(R.drawable.error_poster)
                    .into(poster);

            itemView.setOnClickListener(v -> onClickListener.onMovieClicked(movie));

            title.setText(movie.originalTitle);
            rating.setRating((float) movie.votesAverage);

            if (itemId == R.layout.item_movie_linear) {

                if (year != null)
                    year.setText(movie.releaseDate.substring(0, 4));

                if (overview != null)
                    overview.setText(movie.overview);

                if (adult != null) {

                    if (movie.isAdult)
                        adult.setVisibility(View.VISIBLE);
                    else
                        adult.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}

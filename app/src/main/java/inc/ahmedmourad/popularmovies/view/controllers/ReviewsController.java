package inc.ahmedmourad.popularmovies.view.controllers;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.Unbinder;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.adapters.ReviewsRecyclerAdapter;
import inc.ahmedmourad.popularmovies.model.database.MovieContract;
import inc.ahmedmourad.popularmovies.model.entities.ReviewsEntity;

/**
 * This's where we display our movie's reviews
 */
public class ReviewsController extends Controller implements ReviewsRecyclerAdapter.OnLongClickListener {

    static final String KEY_TITLE = "title";
    static final String KEY_MOVIE_ID = "movie_id";

    @BindView(R.id.reviews_toolbar)
    Toolbar toolbar;

    @BindView(R.id.reviews_recycler_view)
    RecyclerView recyclerView;

    private String title;
    private long movieId;

    private final List<ReviewsEntity> reviewsList = new ArrayList<>();

    private ReviewsRecyclerAdapter recyclerAdapter;

    private Context context;

    private Unbinder unbinder;

    @SuppressWarnings("WeakerAccess")
    public ReviewsController(@Nullable final Bundle args) {
        super(args);

        if (args != null) {
            title = args.getString(KEY_TITLE);
            movieId = args.getLong(KEY_MOVIE_ID);
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup container) {

        final View view = inflater.inflate(R.layout.controller_reviews, container, false);

        unbinder = ButterKnife.bind(this, view);

        context = view.getContext();

        toolbar.setTitle(title);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(v -> getRouter().popController(this));

        initializeRecyclerView();

        displayData();

        return view;
    }

    /**
     * initialize our recyclerView
     */
    private void initializeRecyclerView() {

        recyclerAdapter = new ReviewsRecyclerAdapter(reviewsList, null, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVerticalScrollBarEnabled(true);
    }

    private void displayData() {

        final Cursor cursor = context.getContentResolver()
                .query(MovieContract.ReviewsEntry.buildReviewsUriWithMovieId(movieId),
                        DetailsController.COLUMNS_REVIEWS,
                        null,
                        null,
                        null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                reviewsList.clear();

                do {

                    reviewsList.add(ReviewsEntity.fromCursor(cursor));

                } while (cursor.moveToNext());

                cursor.close();

                recyclerAdapter.notifyDataSetChanged();

            } else {

                cursor.close();
            }
        }
    }

    @Override
    public void onReviewLongClicked(final ReviewsEntity review) {

        final Bundle bundle = new Bundle();

        bundle.putString(BrowserController.KEY_TITLE, title);
        bundle.putString(BrowserController.KEY_URL, review.url);

        getRouter().pushController(RouterTransaction.with(new BrowserController(bundle)));
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}

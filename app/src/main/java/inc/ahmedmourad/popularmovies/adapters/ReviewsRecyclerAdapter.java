package inc.ahmedmourad.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.ahmedmourad.popularmovies.R;
import inc.ahmedmourad.popularmovies.model.entities.ReviewsEntity;

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ReviewsEntity> reviewsList;
    private final OnFooterClickListener onFooterClickListener;
    private final OnLongClickListener onLongClickListener;

    private final boolean showAllReviews;

    public ReviewsRecyclerAdapter(final List<ReviewsEntity> reviewsList, final OnFooterClickListener onFooterClickListener, final OnLongClickListener onLongClickListener) {
        this.reviewsList = reviewsList;
        this.onFooterClickListener = onFooterClickListener;
        this.onLongClickListener = onLongClickListener;

        showAllReviews = onFooterClickListener == null;
    }

    @Override
    public int getItemViewType(final int position) {

        if (showAllReviews)
            return ViewType.NORMAL;

        return position <= 2 ? ViewType.NORMAL : ViewType.FOOTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup container, final int viewType) {

        if (showAllReviews)
            return new NormalViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.item_review_normal, container, false));

        switch (viewType) {

            case ViewType.NORMAL:
                return new NormalViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.item_review_normal_simple, container, false));

            case ViewType.FOOTER:
                return new FooterViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.item_review_footer, container, false));

            default:
                return new NormalViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.item_review_normal_simple, container, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (showAllReviews || position < 3)
            ((NormalViewHolder) holder).bind(reviewsList.get(position), onLongClickListener);
        else
            ((FooterViewHolder) holder).bind(onFooterClickListener);
    }

    @Override
    public int getItemCount() {

        if (showAllReviews)
            return reviewsList.size();

        return reviewsList.size() <= 3 ? reviewsList.size() : 4;
    }

    @FunctionalInterface
    public interface OnFooterClickListener {
        void onReviewFooterClicked();
    }

    @FunctionalInterface
    public interface OnLongClickListener {
        void onReviewLongClicked(final ReviewsEntity review);
    }

    public interface ViewType {
        int NORMAL = 0;
        int FOOTER = 1;
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author)
        TextView author;

        @BindView(R.id.review_content)
        TextView content;

        NormalViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(final ReviewsEntity review, final OnLongClickListener onLongClickListener) {

            itemView.setOnClickListener(v -> {

                if (content.getEllipsize() != null) {

                    content.setEllipsize(null);
                    content.setMaxLines(Integer.MAX_VALUE);
                    notifyDataSetChanged(); // notifyItemChanged(int) causes some weird glitch (blink)

                } else {

                    content.setEllipsize(TextUtils.TruncateAt.END);
                    content.setMaxLines(4);
                    notifyDataSetChanged();
                }
            });

            itemView.setOnLongClickListener(v -> {
                onLongClickListener.onReviewLongClicked(review);
                return true;
            });

            author.setText(review.author);
            content.setText(review.content);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        FooterViewHolder(final View view) {
            super(view);
        }

        private void bind(final OnFooterClickListener onFooterClickListener) {

            itemView.setOnClickListener(v -> onFooterClickListener.onReviewFooterClicked());
        }
    }
}

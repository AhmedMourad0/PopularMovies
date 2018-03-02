package inc.ahmedmourad.popularmovies.adapters;

import android.net.Uri;
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
import inc.ahmedmourad.popularmovies.model.entities.VideosEntity;

public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.ViewHolder> {

    private final List<VideosEntity> videosList;
    private final OnClickListener onClickListener;


    public VideosRecyclerAdapter(final List<VideosEntity> videosList, final OnClickListener onClickListener) {
        this.videosList = videosList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup container, final int viewType) {

        return new ViewHolder(LayoutInflater.from(container.getContext()).inflate(R.layout.item_video, container, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.bind(videosList.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    @FunctionalInterface
    public interface OnClickListener {
        void onVideoClicked(final VideosEntity video);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_thumbnail)
        ImageView thumbnail;

        @BindView(R.id.video_name)
        TextView name;

        @BindView(R.id.video_type)
        TextView type;

        @BindView(R.id.video_hd)
        ImageView hd;

        ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(final VideosEntity video, final OnClickListener onClickListener) {

            itemView.setOnClickListener(v -> onClickListener.onVideoClicked(video));

            // https://img.youtube.com/vi/dW1BIid8Osg/hqdefault.jpg
            final String thumbnailBaseUrl = "https://img.youtube.com/vi/";
            final String pathSize = "hqdefault.jpg"; // "mqdefault.jpg"

            final Uri uri = Uri.parse(thumbnailBaseUrl).buildUpon()
                    .appendEncodedPath(video.key)
                    .appendEncodedPath(pathSize)
                    .build();

            Picasso.with(itemView.getContext())
                    .load(uri.toString())
                    .placeholder(R.drawable.placeholder_video)
                    .error(R.drawable.error_video)
                    .into(thumbnail);

            name.setText(video.name);
            type.setText(video.type);

            if (video.size >= 720)
                hd.setVisibility(View.VISIBLE);
            else
                hd.setVisibility(View.GONE);
        }
    }
}

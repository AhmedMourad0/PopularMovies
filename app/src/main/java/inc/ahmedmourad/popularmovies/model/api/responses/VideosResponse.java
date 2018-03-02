package inc.ahmedmourad.popularmovies.model.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import inc.ahmedmourad.popularmovies.model.entities.VideosEntity;

public class VideosResponse {

    @SuppressWarnings("WeakerAccess")
    @SerializedName("results")
    public List<VideosEntity> videos;

    public List<VideosEntity> getVideos() {

        for (int i = 0; i < videos.size(); ++i) {

            if (!videos.get(i).site.equals("YouTube"))
                videos.remove(i);
        }

        return videos;
    }
}

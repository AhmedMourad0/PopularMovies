package inc.ahmedmourad.popularmovies.model.api.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import inc.ahmedmourad.popularmovies.model.entities.ReviewsEntity;

public class ReviewsResponse {

    @SerializedName("results")
    public List<ReviewsEntity> reviews;
}

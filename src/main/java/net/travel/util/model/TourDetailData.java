package net.travel.util.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travel.model.Review;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetailData<M,IMG> {

    private M model;

    private List<IMG> imageList;

    private List<Review> reviewList;

    private boolean existsWishList;

    private int rating;

    private List<RatingPercent> ratingPercentList;
}
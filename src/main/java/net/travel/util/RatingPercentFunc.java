package net.travel.util;

import org.springframework.data.repository.query.Param;

public interface RatingPercentFunc {

    Integer sumModelRating(int ratingNumber, int modelId);
}

package net.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travel.util.model.RatingPercent;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAddDto {

    private ReviewDto review;

    private boolean messageError;

    private List<RatingPercent> ratingPercentList;

    private int rating;
}
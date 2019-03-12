package net.travel.service;

import net.travel.dto.ReviewAddDto;
import net.travel.model.Review;

public interface ReviewService {

    ReviewAddDto add(Review review);
}

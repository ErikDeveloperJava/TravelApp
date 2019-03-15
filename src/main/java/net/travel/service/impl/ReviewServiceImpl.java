package net.travel.service.impl;

import net.travel.dto.ReviewAddDto;
import net.travel.dto.ReviewDto;
import net.travel.model.Review;
import net.travel.repository.ReviewRepository;
import net.travel.service.ReviewService;
import net.travel.util.NumberUtil;
import net.travel.util.model.RatingPercent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private NumberUtil numberUtil;

    @Override
    public ReviewAddDto add(Review review) {
        reviewRepository.save(review);
        int hotelId = review.getHotel().getId();
        Integer ratingSum = reviewRepository.sumRatingByHotelId(hotelId);
        int reviewCount = reviewRepository.countByHotelId(hotelId);
        List<RatingPercent> ratingPercentList = new ArrayList<>();
        for (int i = 5; i >0 ; i--) {
            int percent;
            Integer ratingNumberSum = reviewRepository
                    .findSumHotelRatingByRatingNumber(i, hotelId);
            if(ratingNumberSum == null){
                percent = 0;
            }else {
                percent = numberUtil.countPercent(ratingNumberSum,ratingSum);
            }
            ratingPercentList.add(RatingPercent
                    .builder()
                    .percent(percent)
                    .ratingNumber(i)
                    .build());
        }
        return ReviewAddDto
                .builder()
                .review(buildReviewDto(review))
                .ratingPercentList(ratingPercentList)
                .rating(numberUtil.countRating(ratingSum,reviewCount))
                .build();
    }

    public static ReviewDto buildReviewDto(Review review){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return ReviewDto
                .builder()
                .hotel(review.getHotel() != null ? HotelServiceImpl.buildHotelDto(review.getHotel()) : null)
                .id(review.getId())
                .message(review.getMessage())
                .place(review.getPlace() != null ? PlaceServiceImpl.buildPlaceDto(review.getPlace()) : null)
                .rating(review.getRating())
                .sendDate(dateFormat.format(review.getSendDate()))
                .user(UserServiceImpl.userDtoBuilder(review.getUser()))
                .build();
    }
}
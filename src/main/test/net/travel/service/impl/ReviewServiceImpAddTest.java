package net.travel.service.impl;

import net.travel.dto.ReviewAddDto;
import net.travel.model.Hotel;
import net.travel.model.Review;
import net.travel.model.User;
import net.travel.repository.ReviewRepository;
import net.travel.service.ReviewService;
import net.travel.util.NumberUtil;
import net.travel.util.model.RatingPercent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ReviewServiceImpAddTest.ConfigClass.class)
public class ReviewServiceImpAddTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @TestConfiguration
    static class ConfigClass{
        @Bean
        public ReviewService reviewService(){
            return new ReviewServiceImpl();
        }

        @Bean
        public NumberUtil numberUtil(){
            return new NumberUtil();
        }
    }

    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private NumberUtil numberUtil;

    @Test
    public void test(){
        Review review = Review
                .builder()
                .id(2)
                .user(User
                        .builder()
                        .id(1)
                        .registerDate(new Date())
                        .build())
                .sendDate(new Date())
                .hotel(Hotel
                        .builder()
                        .id(1)
                        .build())
                .rating(2)
                .build();
        int ratingSum = 20;
        when(reviewRepository.sumRatingByHotelId(review.getHotel().getId()))
                .thenReturn(ratingSum);
        when(reviewRepository.countByHotelId(review.getHotel().getId()))
                .thenReturn(6);
        Map<Integer,Integer> map = new HashMap<>();
        map.put(5,10);
        map.put(4,null);
        map.put(3,6);
        map.put(2,4);
        map.put(1,null);
        List<RatingPercent> ratingPercentList = new ArrayList<>();
        for (int i = 5; i > 0 ; i--) {
            when(reviewRepository
                    .findSumHotelRatingByRatingNumber(i,review.getHotel().getId()))
                    .thenReturn(map.get(i));
            ratingPercentList.add(RatingPercent
                    .builder()
                    .ratingNumber(i)
                    .percent(numberUtil
                            .countPercent(map.get(i) == null ? 0 : map.get(i),ratingSum))
                    .build());
        }
        ReviewAddDto expectedReviewAddDto = ReviewAddDto
                .builder()
                .review(ReviewServiceImpl.buildReviewDto(review))
                .ratingPercentList(ratingPercentList)
                .rating(numberUtil.countRating(ratingSum,6))
                .build();
        ReviewAddDto actualReviewAddDto = reviewService.add(review);
        verify(reviewRepository).save(review);
        assertEquals(expectedReviewAddDto,actualReviewAddDto);
    }
}
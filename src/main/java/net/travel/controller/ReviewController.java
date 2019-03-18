package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.dto.ReviewAddDto;
import net.travel.form.ReviewForm;
import net.travel.model.Hotel;
import net.travel.model.Place;
import net.travel.model.Review;
import net.travel.service.ReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class ReviewController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/model/review")
    public @ResponseBody
    ResponseEntity addReview(@AuthenticationPrincipal CurrentUser currentUser,
                             @RequestBody @Valid ReviewForm reviewForm, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity
                    .ok(ReviewAddDto
                            .builder()
                            .messageError(true)
                            .build());
        }
        Review review = Review
                .builder()
                .sendDate(new Date())
                .rating(reviewForm.getRating())
                .hotel(reviewForm.getHotelId() != 0 ? Hotel
                        .builder()
                        .id(reviewForm.getHotelId())
                        .build() : null)
                .user(currentUser.getUser())
                .message(reviewForm.getMessage())
                .place(reviewForm.getPlaceId() != 0 ? Place
                        .builder()
                        .id(reviewForm.getPlaceId())
                        .build() : null)
                .build();
        LOGGER.info("{} saved successfully",review);
        ReviewAddDto reviewAddDto = reviewService.add(review);
        return ResponseEntity
                .ok(reviewAddDto);
    }
}

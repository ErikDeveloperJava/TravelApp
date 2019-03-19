package net.travel.util;

import net.travel.dto.SearchDto;
import net.travel.model.Hotel;
import net.travel.model.HotelImage;
import net.travel.model.Review;
import net.travel.service.impl.ReviewServiceImpl;
import net.travel.util.model.RatingPercent;
import net.travel.util.model.TourDetailData;
import net.travel.util.search.SearchFuncInterface;
import net.travel.util.search.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataUtil {

    @Autowired
    private NumberUtil numberUtil;

    public <M,IL> TourDetailData<M,IL> getModelDetailById(int userId,int modelId,
                                                          DetailFuncInterface<M,IL> detailFuncInterface){
        return detailFuncInterface
                .getDetailById(modelId, userId);
    }

    public <T> SearchDto<T> getSearchModel(SearchParam searchParam, Pageable pageable,
                                           int userId, SearchFuncInterface<T> searchFuncInterface){
        return searchFuncInterface.getByParams(searchParam, pageable,userId);
    }

    public <M,IL> TourDetailData<M,IL> buildTourDetail(int modelId, Integer ratingSum,
                                                       RatingPercentFunc ratingPercentFunc, M model, List<IL>
                                                               hotelImageList, List<Review> reviewList,int ratingCount,boolean existsWishList){
        List<RatingPercent> ratingPercentList = ReviewServiceImpl
                .countRatingPercent(modelId,ratingSum == null ? 0 : ratingSum,numberUtil,ratingPercentFunc);
        return TourDetailData.<M, IL>
                builder()
                .model(model)
                .imageList(hotelImageList)
                .reviewList(reviewList)
                .rating(numberUtil.countRating(ratingSum,ratingCount))
                .existsWishList(existsWishList)
                .ratingPercentList(ratingPercentList)
                .build();
    }
}
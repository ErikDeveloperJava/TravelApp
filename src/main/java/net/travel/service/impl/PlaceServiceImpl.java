package net.travel.service.impl;

import net.travel.dto.PlaceDto;
import net.travel.dto.SearchDto;
import net.travel.model.*;
import net.travel.repository.PlaceImageRepository;
import net.travel.repository.PlaceRepository;
import net.travel.repository.ReviewRepository;
import net.travel.repository.WishListRepository;
import net.travel.service.PlaceService;
import net.travel.util.NumberUtil;
import net.travel.util.PaginationUtil;
import net.travel.util.model.RatingPercent;
import net.travel.util.model.TourData;
import net.travel.util.model.TourDetailData;
import net.travel.util.search.SearchParam;
import net.travel.util.search.SearchParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private NumberUtil numberUtil;

    @Autowired
    private SearchParser searchParser;

    @Autowired
    private PaginationUtil paginationUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlaceImageRepository placeImageRepository;

    @Override
    public List<TourData<Place>> getByHighestRating(int userId, Pageable pageable) {
        List<Place> placeList = placeRepository.findHighestRating(pageable);
        List<TourData<Place>> tourDataList = new ArrayList<>();
        for (Place place : placeList) {
            Integer sumRating = reviewRepository.sumRatingByPlaceId(place.getId());
            int count = reviewRepository.countByPlaceId(place.getId());
            tourDataList.add(TourData
                    .<Place>builder()
                    .tourPlace(place)
                    .rating(numberUtil.countRating(sumRating, count))
                    .existsWishList(userId != -1 && wishListRepository.existsByUser_idAndPlace_id(userId, place.getId()))
                    .build());
        }
        return tourDataList;
    }

    @Override
    public List<PlaceDto> getAllByDto() {
        List<Place> placeList = placeRepository.findAll();
        List<PlaceDto> placeDtoList = new ArrayList<>();
        for (Place place : placeList) {
            placeDtoList.add(buildPlaceDto(place));
        }
        return placeDtoList;
    }

    @Override
    public SearchDto<PlaceDto> getByParams(SearchParam searchParam, Pageable pageable, int userId) {
        String countQuery = searchParser.buildQuery(searchParam, Optional.empty());
        BigInteger bigInteger = (BigInteger) entityManager.createNativeQuery(countQuery)
                .getResultList().get(0);
        int count = bigInteger.intValue();
        int length = paginationUtil.getPaginationLength(count, pageable.getPageSize());
        pageable = paginationUtil.checkPageableObject(pageable, length);
        String placeQuery = searchParser.buildQuery(searchParam, Optional.of(pageable));
        List<Place> placeList = entityManager.createNativeQuery(placeQuery, Place.class)
                .getResultList();
        List<TourData<PlaceDto>> tourDataList = new ArrayList<>();
        for (Place place : placeList) {
            Integer ratingSum = reviewRepository.sumRatingByPlaceId(place.getId());
            int ratingCount = reviewRepository.countByPlaceId(place.getId());
            TourData<PlaceDto> tourData = TourData.
                    <PlaceDto>builder()
                    .tourPlace(buildPlaceDto(place))
                    .rating(numberUtil.countRating(ratingSum, ratingCount))
                    .existsWishList(userId != -1 && wishListRepository.existsByUser_idAndPlace_id(userId, place.getId()))
                    .build();
            tourDataList.add(tourData);
        }
        return SearchDto.<PlaceDto>
                builder()
                .modelList(tourDataList)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .paginationLength(length)
                .build();


    }

    @Override
    public TourDetailData<Place, PlaceImage> getDetailById(int id, int userId) {
        Place place = placeRepository.findById(id).get();
        place.getHotelList();
        List<PlaceImage> placeImageList = placeImageRepository.findAllByPlace_Id(id);
        List<Review> reviewList = reviewRepository.findByPlace_Id(id);
        Integer ratingSum = reviewRepository.sumRatingByPlaceId(id);
        int ratingCount = reviewRepository.countByPlaceId(id);
        boolean existsWishList = userId != -1 && wishListRepository
                .existsByUser_idAndPlace_id(userId, id);
        List<RatingPercent> ratingPercentList = ReviewServiceImpl
                .countRatingPercent(id,ratingSum == null ? 0 : ratingSum,numberUtil,reviewRepository::findSumPlaceRatingByRatingNumber);
        return TourDetailData.<Place,PlaceImage>
                builder()
                .model(place)
                .imageList(placeImageList)
                .reviewList(reviewList)
                .rating(numberUtil.countRating(ratingSum, ratingCount))
                .existsWishList(existsWishList)
                .ratingPercentList(ratingPercentList)
                .build();
    }

    @Override
    public boolean existsById(int placeId) {
        return placeRepository.existsById(placeId);
    }

    public static PlaceDto buildPlaceDto(Place place) {
        return PlaceDto
                .builder()
                .id(place.getId())
                .contact(place.getContact())
                .imgUrl(place.getImgUrl())
                .information(place.getInformation())
                .name(place.getName())
                .price(place.getPrice())
                .build();
    }
}

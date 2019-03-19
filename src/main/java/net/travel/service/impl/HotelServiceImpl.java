package net.travel.service.impl;

import net.travel.dto.HotelDto;
import net.travel.dto.SearchDto;
import net.travel.model.Hotel;
import net.travel.model.HotelImage;
import net.travel.model.HotelRoom;
import net.travel.model.Review;
import net.travel.repository.HotelImageRepository;
import net.travel.repository.HotelRepository;
import net.travel.repository.ReviewRepository;
import net.travel.repository.WishListRepository;
import net.travel.service.HotelService;
import net.travel.util.DataUtil;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private NumberUtil numberUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SearchParser searchParser;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private HotelImageRepository hotelImageRepository;

    @Autowired
    private DataUtil dataUtil;

    @Override
    public List<TourData<Hotel>> getByHighestRating(int userId, Pageable pageable) {
        List<TourData<Hotel>> tourDataList = new ArrayList<>();
        List<Hotel> hotelList = hotelRepository.findByHighestRating(pageable);
        for (Hotel hotel : hotelList) {
            Integer ratingSum = reviewRepository.sumRatingByHotelId(hotel.getId());
            int ratingCount = reviewRepository.countByHotelId(hotel.getId());
            tourDataList.add(TourData
                    .<Hotel>builder()
                    .tourPlace(hotel)
                    .rating(numberUtil.countRating(ratingSum,ratingCount))
                    .existsWishList(userId != -1 && wishListRepository.existsByUser_idAndHotel_id(userId,hotel.getId()))
                    .build());
        }
        return tourDataList;
    }

    @Override
    public SearchDto<HotelDto> getByParams(SearchParam searchParam,
                                                 Pageable pageable,int userId) {
        String countQuery = searchParser.buildQuery(searchParam, Optional.empty());
        BigInteger bigInteger = (BigInteger)entityManager.createNativeQuery(countQuery)
                .getResultList().get(0);
        int count = bigInteger.intValue();
        int length = paginationUtil.getPaginationLength(count, pageable.getPageSize());
        pageable = paginationUtil.checkPageableObject(pageable,length);
        String getHotelsQuery = searchParser.buildQuery(searchParam, Optional.of(pageable));
        List<Hotel> hotelList = entityManager.createNativeQuery(getHotelsQuery,Hotel.class)
                .getResultList();
        List<TourData<HotelDto>> tourDataList = new ArrayList<>();
        for (Hotel hotel : hotelList) {
            Integer ratingSum = reviewRepository.sumRatingByHotelId(hotel.getId());
            int ratingCount = reviewRepository.countByHotelId(hotel.getId());
            TourData<HotelDto> tourData = TourData.
                    <HotelDto>builder()
                    .tourPlace(buildHotelDto(hotel))
                    .rating(numberUtil.countRating(ratingSum,ratingCount))
                    .existsWishList(userId != -1 && wishListRepository.existsByUser_idAndHotel_id(userId,hotel.getId()))
                    .build();
            tourDataList.add(tourData);
        }
        return SearchDto.<HotelDto>
                builder()
                .modelList(tourDataList)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .paginationLength(length)
                .build();
    }

    @Transactional
    public TourDetailData<Hotel, HotelImage> getDetailById(int id, int userId) {
        Hotel hotel = hotelRepository.findById(id).get();
        hotel.getHotelRoomList();
        List<HotelImage> hotelImageList = hotelImageRepository.findByHotel_Id(id);
        List<Review> reviewList = reviewRepository.findByHotel_Id(id);
        Integer ratingSum = reviewRepository.sumRatingByHotelId(id);
        int ratingCount = reviewRepository.countByHotelId(id);
        boolean existsWishList = userId != -1 && wishListRepository
                .existsByUser_idAndHotel_id(userId,hotel.getId());
        return dataUtil.buildTourDetail(id,ratingSum,reviewRepository::findSumHotelRatingByRatingNumber,hotel,
                hotelImageList,reviewList,ratingCount,existsWishList);
    }

    @Override
    public boolean existsById(int hotelId) {
        return hotelRepository.existsById(hotelId);
    }

    public static HotelDto buildHotelDto(Hotel hotel){
        return HotelDto
                .builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .information(hotel.getInformation())
                .imgUrl(hotel.getImgUrl())
                .placeDto(hotel.getPlace() != null ? PlaceServiceImpl
                        .buildPlaceDto(hotel.getPlace()) : null)
                .contact(hotel.getContact())
                .build();
    }
}

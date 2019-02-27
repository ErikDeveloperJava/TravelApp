package net.travel.service;

import net.travel.dto.HotelDto;
import net.travel.dto.SearchDto;
import net.travel.model.Hotel;
import net.travel.model.HotelImage;
import net.travel.util.model.TourData;
import net.travel.util.model.TourDetailData;
import net.travel.util.search.SearchParam;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HotelService {

    List<TourData<Hotel>> getByHighestRating(int userId, Pageable pageable);

    SearchDto<HotelDto> getByParams(SearchParam searchParam, Pageable pageable,int userId);

    TourDetailData<Hotel, HotelImage> getDetailById(int id,int userId);

    boolean existsById(int hotelId);
}
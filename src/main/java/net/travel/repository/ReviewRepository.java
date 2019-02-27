package net.travel.repository;

import net.travel.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    @Query("select sum(r.rating)+0 from Review r where r.place.id=:placeId")
    Integer sumRatingByPlaceId(@Param("placeId")int placeId);

    int countByPlaceId(int placeId);

    @Query("select sum(r.rating)+0 from Review r where r.hotel.id=:hotelId")
    Integer sumRatingByHotelId(@Param("hotelId")int hotelId);

    int countByHotelId(int hotelId);

    List<Review> findByHotel_Id(int hotelId);

    List<Review> findByPlace_Id(int placeId);
}
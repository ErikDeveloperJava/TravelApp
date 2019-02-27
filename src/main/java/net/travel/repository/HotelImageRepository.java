package net.travel.repository;

import net.travel.model.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelImageRepository extends JpaRepository<HotelImage,Integer> {

    List<HotelImage> findByHotel_Id(int hotelId);
}

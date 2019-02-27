package net.travel.repository;

import net.travel.model.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Integer> {

    @Query(value = "select * from hotel h order by (select sum(r.rating) from review r where r.hotel_id=h.id)/(select count(*) from review r where r.hotel_id=h.id) desc",nativeQuery = true)
    List<Hotel> findByHighestRating(Pageable pageable);
}
package net.travel.repository;

import net.travel.model.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Integer> {

    @Query(value = "select * from place p order by (select sum(r.rating) from review r where r.place_id=p.id)/(select count(*) from review r where r.place_id=p.id) desc",nativeQuery = true)
    List<Place> findHighestRating(Pageable pageable);
}
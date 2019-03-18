package net.travel.repository;

import net.travel.model.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceImageRepository extends JpaRepository<PlaceImage,Integer> {

    List<PlaceImage> findAllByPlace_Id(int placeId);
}

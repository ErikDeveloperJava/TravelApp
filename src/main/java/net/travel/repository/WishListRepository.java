package net.travel.repository;

import net.travel.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList,Integer> {

    int countByUser_id(int userId);

    boolean existsByUser_idAndPlace_id(int userId,int placeId);

    boolean existsByUser_idAndHotel_id(int userId,int hotelId);

    Optional<WishList> findByUser_idAndPlace_id(int userId,int placeId);

    Optional<WishList> findByUser_idAndHotel_id(int userId,int hotelId);
}
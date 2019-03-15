package net.travel.repository;

import net.travel.model.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRoomRepository extends JpaRepository<HotelRoom,Integer> {
}

package net.travel.service;

import net.travel.model.HotelRoom;

import java.util.Optional;

public interface HotelRoomService {

    Optional<HotelRoom> getById(int hotelRoomId);

    boolean existsById(int hotelId);
}
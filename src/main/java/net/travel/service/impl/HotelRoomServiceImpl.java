package net.travel.service.impl;

import net.travel.dto.HotelRoomDto;
import net.travel.model.HotelRoom;
import net.travel.repository.HotelRoomRepository;
import net.travel.service.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @Transactional
    public Optional<HotelRoom> getById(int hotelRoomId,boolean isLoadImageList) {
        Optional<HotelRoom> hotelRoomOptional = hotelRoomRepository.findById(hotelRoomId);
        if(hotelRoomOptional.isPresent() && isLoadImageList){
            hotelRoomOptional.get().getImageList();
        }
        return hotelRoomOptional;
    }

    @Override
    public boolean existsById(int hotelId) {
        return hotelRoomRepository.existsById(hotelId);
    }

    public static HotelRoomDto buildHotelRoomDto(HotelRoom hotelRoom){
        return HotelRoomDto
                .builder()
                .busiedCount(hotelRoom.getBusiedCount())
                .count(hotelRoom.getCount())
                .hotel(HotelServiceImpl.buildHotelDto(hotelRoom.getHotel()))
                .hotelRoomAttribute(hotelRoom.getHotelRoomAttribute())
                .id(hotelRoom.getId())
                .imgUrl(hotelRoom.getImgUrl())
                .information(hotelRoom.getInformation())
                .name(hotelRoom.getName())
                .price(hotelRoom.getPrice())
                .build();
    }
}

package net.travel.controller;

import net.travel.model.HotelRoom;
import net.travel.service.HotelRoomService;
import net.travel.service.impl.HotelRoomServiceImpl;
import net.travel.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/hotel/room")
public class HotelRoomController {

    @Autowired
    private HotelRoomService hotelRoomService;

    @Autowired
    private NumberUtil numberUtil;

    @GetMapping("/id/{hotelRoomId}")
    public @ResponseBody
    ResponseEntity hotelRoomById(@PathVariable("hotelRoomId") String strHotelRoomId){
        int hotelRoomId = numberUtil.parseStrToInteger(strHotelRoomId);
        Optional<HotelRoom> optionalHotelRoom;
        if(hotelRoomId == -1 || !((optionalHotelRoom = hotelRoomService.getById(hotelRoomId)).isPresent())){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        return ResponseEntity
                .ok(HotelRoomServiceImpl.buildHotelRoomDto(optionalHotelRoom.get()));
    }
}
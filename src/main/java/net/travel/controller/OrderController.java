package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.dto.OrderDto;
import net.travel.form.OrderForm;
import net.travel.model.Hotel;
import net.travel.model.HotelRoom;
import net.travel.model.UserOrder;
import net.travel.service.HotelRoomService;
import net.travel.service.HotelService;
import net.travel.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private OrderService orderService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelRoomService hotelRoomService;

    @PostMapping
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderForm orderForm, BindingResult result,
                         @AuthenticationPrincipal CurrentUser currentUser){
        List<String> errorList = new ArrayList<>();
        boolean success = false;
        Optional<HotelRoom> optionalHotelRoom;
        if(result.hasErrors()){
            for (FieldError fieldError : result.getFieldErrors()) {
                errorList.add(fieldError.getField());
            }
        }else if(orderForm.getAdultCount() <= 0 && orderForm.getChildrenCount() <= 0){
            errorList.add("adultCount");
            errorList.add("childrenCount");
        }else if(!hotelService.existsById(orderForm.getHotelId())){
            errorList.add("hotelId");
        }else if(!(optionalHotelRoom = hotelRoomService.getById(orderForm.getHotelRoomId())).isPresent()){
            errorList.add("hotelRoomId");
        }else {
            HotelRoom hotelRoom = optionalHotelRoom.get();
            if(hotelRoom.getBusiedCount() >= hotelRoom.getCount() ){
                errorList.add("allRoomsBusied");
            }else {
                double generalPrice = hotelRoom.getPrice() * orderForm.getDaysCount();
                hotelRoom.setBusiedCount(hotelRoom.getBusiedCount() + 1);
                UserOrder userOrder = UserOrder
                        .builder()
                        .user(currentUser.getUser())
                        .hotelRoom(hotelRoom)
                        .orderDate(new Date())
                        .hotel(Hotel
                                .builder()
                                .id(orderForm.getHotelId())
                                .build())
                        .childrenCount(orderForm.getChildrenCount())
                        .adultCount(orderForm.getAdultCount())
                        .price(generalPrice)
                        .howManyDays(orderForm.getDaysCount())
                        .whenDate(orderForm.getWhenDate())
                        .build();
                orderService.add(userOrder);
                LOGGER.info("{} : order saved successfully",userOrder);
                success = true;
            }
        }
        return ResponseEntity
                .ok(OrderDto
                        .builder()
                        .errorList(errorList)
                        .success(success)
                        .build());

    }
}
package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.model.HotelRoom;
import net.travel.model.UserOrder;
import net.travel.service.HotelRoomService;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
import net.travel.service.impl.HotelRoomServiceImpl;
import net.travel.util.NumberUtil;
import net.travel.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private WishListService wishListService;

    @GetMapping("/id/{hotelRoomId}")
    public @ResponseBody
    ResponseEntity hotelRoomById(@PathVariable("hotelRoomId") String strHotelRoomId) {
        int hotelRoomId = numberUtil.parseStrToInteger(strHotelRoomId);
        Optional<HotelRoom> optionalHotelRoom;
        if (hotelRoomId == -1 || !((optionalHotelRoom = hotelRoomService.getById(hotelRoomId, false)).isPresent())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        return ResponseEntity
                .ok(HotelRoomServiceImpl.buildHotelRoomDto(optionalHotelRoom.get()));
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String strId,
                         @AuthenticationPrincipal CurrentUser currentUser, Model model) {
        int id = numberUtil.parseStrToInteger(strId);
        Optional<HotelRoom> hotelRoomOptional;
        if(id == -1 || !(hotelRoomOptional= hotelRoomService.getById(id, true)).isPresent()){
            return "redirect:/";
        }
        boolean isUserExist = userService.isNotNull(currentUser);
        if (isUserExist) {
            model.addAttribute("currentUser", currentUser.getUser());
            model.addAttribute("bookingCount", userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount", wishListService.countByUserId(currentUser.getUser().getId()));
        }
        model.addAttribute("hotelRoom",hotelRoomOptional.get());
        return TemplateUtil.HOTEL_ROOM_DETAIL;
    }
}
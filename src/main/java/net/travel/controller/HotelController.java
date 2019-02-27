package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.dto.HotelDto;
import net.travel.dto.SearchDto;
import net.travel.model.Hotel;
import net.travel.model.HotelImage;
import net.travel.service.HotelService;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
import net.travel.util.NumberUtil;
import net.travel.util.TemplateUtil;
import net.travel.util.model.TourDetailData;
import net.travel.util.search.SearchModelType;
import net.travel.util.search.SearchParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HotelController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private NumberUtil numberUtil;

    @GetMapping("/hotels")
    public String hotelsTemplate(@AuthenticationPrincipal CurrentUser currentUser,
                                 Model model){
        boolean isUserExist = userService.isNotNull(currentUser);
        if(isUserExist){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }
        return TemplateUtil.HOTELS;
    }

    @GetMapping("/hotels/search")
    public @ResponseBody
    ResponseEntity hotelsSearch(@RequestParam(value = "name",required = false,defaultValue = "-1")String name,
                                @RequestParam(value = "place",required = false,defaultValue = "-1")String place,
                                @RequestParam(value = "roomsCount",required = false,defaultValue = "-1")String roomsCount,
                                @RequestParam(value = "region",required = false,defaultValue = "-1")String region,
                                @RequestParam(value = "city",required = false,defaultValue = "-1")String city,
                                @RequestParam(value = "order",required = false,defaultValue = "new")String order,
                                Pageable pageable,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        SearchParam searchParam = SearchParam
                .builder()
                .modelType(SearchModelType.HOTEL)
                .name(name)
                .place(place)
                .roomsCount(roomsCount)
                .price("-1")
                .region(region)
                .city(city)
                .order(order)
                .userId(-1)
                .build();
        boolean isUserExist = userService.isNotNull(currentUser);
        SearchDto<HotelDto> searchDto = hotelService.getByParams(searchParam, pageable,
                isUserExist ? currentUser.getUser().getId() : -1);
        return ResponseEntity
                .ok(searchDto);
    }

    @GetMapping("/hotel/detail/{hotelId}")
    private String hotelDetail(@AuthenticationPrincipal CurrentUser currentUser,
                               Model model, @PathVariable("hotelId")String strHotelId){
        int hotelId = numberUtil.parseStrToInteger(strHotelId);
        if (hotelId != -1 && !hotelService.existsById(hotelId)) {
            return "redirect:/";
        }
        boolean isUserExist = userService.isNotNull(currentUser);
        if(isUserExist){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }
        TourDetailData<Hotel, HotelImage> hotelDetail = hotelService
                .getDetailById(hotelId, isUserExist ? currentUser.getUser().getId() : -1);
        model.addAttribute("tourDetail",hotelDetail);
        return TemplateUtil.HOTEL_DETAIL;
    }
}
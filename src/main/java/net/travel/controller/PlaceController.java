package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.dto.HotelDto;
import net.travel.dto.PlaceDto;
import net.travel.dto.SearchDto;
import net.travel.service.PlaceService;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
import net.travel.util.TemplateUtil;
import net.travel.util.search.SearchModelType;
import net.travel.util.search.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private WishListService wishListService;

    @GetMapping("/place")
    public @ResponseBody
    ResponseEntity places(){
        List<PlaceDto> placeDtoList = placeService.getAllByDto();
        return ResponseEntity
                .ok(placeDtoList);
    }

    @GetMapping("/places")
    public String placesPage(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        boolean isUserExist = userService.isNotNull(currentUser);
        if(isUserExist){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }

        return TemplateUtil.PLACES;
    }

    @GetMapping("/places/search")
    public @ResponseBody
    ResponseEntity hotelsSearch(@RequestParam(value = "name",required = false,defaultValue = "-1")String name,
                                @RequestParam(value = "region",required = false,defaultValue = "-1")String region,
                                @RequestParam(value = "city",required = false,defaultValue = "-1")String city,
                                @RequestParam(value = "order",required = false,defaultValue = "new")String order,
                                Pageable pageable,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        SearchParam searchParam = SearchParam
                .builder()
                .modelType(SearchModelType.PLACE)
                .name(name)
                .place("-1")
                .roomsCount("-1")
                .price("-1")
                .region(region)
                .city(city)
                .order(order)
                .userId(-1)
                .build();
        boolean isUserExist = userService.isNotNull(currentUser);
        SearchDto<PlaceDto> searchDto = placeService.getByParams(searchParam, pageable,
                isUserExist ? currentUser.getUser().getId() : -1);
        return ResponseEntity
                .ok(searchDto);
    }
}
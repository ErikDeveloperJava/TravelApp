package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.dto.*;
import net.travel.service.*;
import net.travel.util.TemplateUtil;
import net.travel.util.search.SearchModelType;
import net.travel.util.search.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/wish_list")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private HotelService hotelService;

    @PostMapping
    public @ResponseBody
    ResponseEntity add(@RequestBody WishListAddDto wishListAddDto,
                       @AuthenticationPrincipal CurrentUser currentUser){
        boolean result = wishListService.add(currentUser.getUser().getId(), wishListAddDto);
        return ResponseEntity
                .ok(new BooleanDto(result));
    }

    @GetMapping("/search")
    public @ResponseBody
    ResponseEntity hotelsSearch(@RequestParam(value = "name",required = false,defaultValue = "-1")String name,
                                @RequestParam(value = "region",required = false,defaultValue = "-1")String region,
                                @RequestParam(value = "city",required = false,defaultValue = "-1")String city,
                                @RequestParam(value = "order",required = false,defaultValue = "new")String order,
                                @RequestParam(value = "type",required = false,defaultValue = "hotel")String type,
                                Pageable pageable,
                                @AuthenticationPrincipal CurrentUser currentUser) {
        SearchParam searchParam = SearchParam
                .builder()
                .modelType(type.equals("place") ? SearchModelType.PLACE : SearchModelType.HOTEL)
                .name(name)
                .place("-1")
                .roomsCount("-1")
                .price("-1")
                .region(region)
                .city(city)
                .order(order)
                .userId(currentUser.getUser().getId())
                .build();
        boolean isUserExist = userService.isNotNull(currentUser);
        SearchDto searchDto;
        if(searchParam.getModelType() == SearchModelType.PLACE){
            searchDto = placeService.getByParams(searchParam, pageable,
                    isUserExist ? currentUser.getUser().getId() : -1);
        }else {
            searchDto = hotelService.getByParams(searchParam, pageable,
                    isUserExist ? currentUser.getUser().getId() : -1);
        }
        return ResponseEntity
                .ok(searchDto);
    }


    @GetMapping
    public String placesPage(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        boolean isUserExist = userService.isNotNull(currentUser);
        if(isUserExist){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }
        return TemplateUtil.WISH_LIST;
    }
}
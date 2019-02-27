package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.model.enums.UserType;
import net.travel.service.*;
import net.travel.util.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("")
    public String main(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        if(currentUser != null && currentUser.getUser().getUserType() == UserType.ADMIN){
            return "redirect:/admin";
        }
        boolean isUserExist = userService.isNotNull(currentUser);
        if(isUserExist){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }
        model.addAttribute("popularPlaceList",placeService.getByHighestRating(isUserExist ?
                currentUser.getUser().getId(): -1, PageRequest.of(0,6)));
        model.addAttribute("popularHotelList",hotelService.getByHighestRating(isUserExist ?
                currentUser.getUser().getId(): -1, PageRequest.of(0,4)));
        return TemplateUtil.INDEX;
    }
}
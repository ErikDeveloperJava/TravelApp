package net.travel.util;

import net.travel.config.security.CurrentUser;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class AuthenticationUtil {

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private WishListService wishListService;

    public void addUserDataInModel(CurrentUser currentUser, Model model){
        if(currentUser != null){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }
    }
}
package net.travel.controller;

import net.travel.config.security.CurrentUser;
import net.travel.dto.ImageDto;
import net.travel.model.UserOrder;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
import net.travel.util.ImageUtil;
import net.travel.util.PaginationUtil;
import net.travel.util.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private PaginationUtil paginationUtil;

    @GetMapping("/detail")
    public String userDetail(@AuthenticationPrincipal CurrentUser currentUser,
                             Model model) {
        model.addAttribute("currentUser", currentUser.getUser());
        model.addAttribute("bookingCount", userOrderService.countByUserId(currentUser.getUser().getId()));
        model.addAttribute("wishListCount", wishListService.countByUserId(currentUser.getUser().getId()));
        return TemplateUtil.USER_DETAIL;
    }

    @PostMapping("/image/delete")
    public @ResponseBody
    ResponseEntity deleteUserImage(@AuthenticationPrincipal CurrentUser currentUser) {
        userService.deleteUserImage(currentUser);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/image/change")
    public @ResponseBody
    ResponseEntity changeUserImage(MultipartFile image,
                                   @AuthenticationPrincipal CurrentUser currentUser) {
        if (!imageUtil.isValidFormat(image.getContentType())) {
            return ResponseEntity
                    .ok(ImageDto
                            .builder()
                            .imageFormatError(true)
                            .build());
        }
        String imgUrl = userService.changeUserImage(currentUser, image);
        return ResponseEntity
                .ok(ImageDto
                        .builder()
                        .imgUrl(imgUrl)
                        .build());
    }

    @GetMapping("/booking")
    public String userBooking(@AuthenticationPrincipal CurrentUser currentUser, Model model,
                              Pageable pageable){
        int userOrdersCount = userOrderService.countByUserId(currentUser.getUser().getId());
        int paginationLength = paginationUtil.getPaginationLength(userOrdersCount, pageable.getPageSize());
        pageable = paginationUtil.checkPageableObject(pageable,paginationLength);
        boolean isUserExist = userService.isNotNull(currentUser);
        if(isUserExist){
            model.addAttribute("currentUser",currentUser.getUser());
            model.addAttribute("bookingCount",userOrderService.countByUserId(currentUser.getUser().getId()));
            model.addAttribute("wishListCount",wishListService.countByUserId(currentUser.getUser().getId()));
        }
        model.addAttribute("currentPageNumber",pageable.getPageNumber());
        model.addAttribute("paginationLength",paginationLength);
        List<UserOrder> userOrderList = userService.getUserOrders(currentUser.getUser(), pageable);
        model.addAttribute("userOrderList",userOrderList);
        return TemplateUtil.USER_BOOKING;
    }
}
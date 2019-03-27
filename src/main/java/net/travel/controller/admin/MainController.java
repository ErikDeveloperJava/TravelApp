package net.travel.controller.admin;

import net.travel.config.security.CurrentUser;
import net.travel.model.UserOrder;
import net.travel.service.UserOrderService;
import net.travel.util.AuthenticationUtil;
import net.travel.util.PaginationUtil;
import net.travel.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminMainController")
@RequestMapping("/admin")
public class MainController {

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @GetMapping
    public String main(Model model, Pageable pageable, @AuthenticationPrincipal CurrentUser currentUser){
        int count = userOrderService.count();
        int paginationLength = paginationUtil.getPaginationLength(count, pageable.getPageSize());
        pageable = paginationUtil.checkPageableObject(pageable,paginationLength);
        authenticationUtil.addUserDataInModel(currentUser,model);
        List<UserOrder> userOrderList = userOrderService.getAll(pageable);
        model.addAttribute("userOrderList",userOrderList);
        model.addAttribute("paginationLength",paginationLength);
        model.addAttribute("currentPageNumber",pageable.getPageNumber());
        return TemplateUtil.ADMIN_INDEX;
    }
}
package net.travel.controller.admin;

import net.travel.dto.SendMessageDto;
import net.travel.form.SendMessage;
import net.travel.model.UserOrder;
import net.travel.service.MailService;
import net.travel.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MailService mailService;

    @PostMapping("/send-email-message")
    public @ResponseBody
    ResponseEntity sendMessageToEmail(@RequestBody @Valid SendMessage sendMessage,
                                      BindingResult result){
        SendMessageDto responseObject = new SendMessageDto();
        Optional<UserOrder> optionalUserOrder;
        if(result.hasErrors()){
            result.getFieldErrors().forEach(fieldError -> responseObject.getErrorList().add(fieldError.getField()));
        }else if(!(optionalUserOrder = orderService.getById(sendMessage.getOrderId())).isPresent()){
            responseObject.getErrorList().add("orderId");
        }else {
            UserOrder userOrder = optionalUserOrder.get();
            String userEmail = userOrder.getUser().getEmail();
            userOrder.setSendMessage(true);
            orderService.update(userOrder);
            responseObject.setSuccess(true);
            mailService.send(userEmail,sendMessage.getMessage(),sendMessage.getSubject());
        }
        return ResponseEntity
                .ok(responseObject);
    }
}
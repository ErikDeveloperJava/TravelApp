package net.travel.controller;

import net.travel.form.RestorePasswordForm;
import net.travel.model.User;
import net.travel.model.enums.UserStatus;
import net.travel.service.MailService;
import net.travel.service.UserService;
import net.travel.util.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/forgot_password")
public class ForgotPasswordController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MailService mailService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String forgotPasswordGet(){
        return TemplateUtil.FORGOT_PASSWORD;
    }

    @PostMapping
    public String forgotPasswordPost(String email, Model model, Locale locale){
        Optional<User> optionalUser = userService.getByEmail(email);
        if(!optionalUser.isPresent()){
            model.addAttribute("emailNotExist","");
            return TemplateUtil.FORGOT_PASSWORD;
        }
        User user = optionalUser.get();
        if(user.getUserStatus() == UserStatus.INACTIVE){
            model.addAttribute("emailInactive","");
            return TemplateUtil.FORGOT_PASSWORD;
        }
        String subject = messageSource.getMessage("mail.message",null,locale);
        String token = UUID.randomUUID().toString();
        String text = "http://localhost:8080/forgot_password/restore_password/" + email + "/" + token;
        user.setUserStatus(UserStatus.INACTIVE);
        user.setToken(token);
        userService.update(user);
        mailService.send(email,text,subject);
        LOGGER.info("{} want to restore password",user);
        return "redirect:/forgot_password?confirm_email";
    }

    @GetMapping("/restore_password/{email}/{token}")
    public String confirmEmail(@PathVariable("email")String email,
                               @PathVariable("token")String token,
                               Model model){
        User user = RegisterController.checkTokenAndEmail(email,token,userService);
        if(user != null){
            RestorePasswordForm restorePasswordForm = RestorePasswordForm
                    .builder()
                    .email(email)
                    .token(token)
                    .build();
            model.addAttribute("restorePasswordForm",restorePasswordForm);
            return TemplateUtil.RESTORE_PASSWORD;
        }
        return "redirect:/";
    }

    @PostMapping("/restore_password")
    public String restorePassword(@Valid  RestorePasswordForm restorePasswordForm,
                                  BindingResult result,Model model){
        User user = RegisterController.checkTokenAndEmail(restorePasswordForm.getEmail(),
                restorePasswordForm.getToken(), userService);
        if(user == null){
            return "redirect:/";
        }
        if(result.hasErrors()){
            return TemplateUtil.RESTORE_PASSWORD;
        }
        if(!restorePasswordForm.getPassword().equals(restorePasswordForm.getRePassword())){
            model.addAttribute("passwordNotMatches","");
            return TemplateUtil.RESTORE_PASSWORD;
        }
        user.setToken(null);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(restorePasswordForm.getPassword()));
        userService.update(user);
        LOGGER.info("{} restored password");
        return "redirect:/login";
    }
}
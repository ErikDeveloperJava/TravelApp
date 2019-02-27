package net.travel.controller;

import net.travel.form.ContactForm;
import net.travel.form.UserAddForm;
import net.travel.model.City;
import net.travel.model.Contact;
import net.travel.model.Region;
import net.travel.model.User;
import net.travel.model.enums.UserStatus;
import net.travel.model.enums.UserType;
import net.travel.service.MailService;
import net.travel.service.UserService;
import net.travel.util.ImageUtil;
import net.travel.util.TemplateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RegisterController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerPost(Model model){
        model.addAttribute("userAddForm",new UserAddForm());
        return TemplateUtil.REGISTER;
    }

    @PostMapping("/register")
    public String registerPost(@Valid UserAddForm userAddForm,
                               BindingResult result, Model model, Locale locale){
        if(result.hasErrors()){
            return TemplateUtil.REGISTER;
        }
        if(userService.existsByEmail(userAddForm.getEmail())){
            model.addAttribute("emailExist","");
            return TemplateUtil.REGISTER;
        }
        if(!userAddForm.getPassword().equals(userAddForm.getRePassword())){
            model.addAttribute("passwordNotMatch","");
            return TemplateUtil.REGISTER;
        }
        if(userAddForm.getImage() != null && !userAddForm.getImage().isEmpty() && !imageUtil.isValidFormat(userAddForm.getImage().getContentType())){
            result.addError(new FieldError("userAddForm","image",""));
            return TemplateUtil.REGISTER;
        }
        String token = UUID.randomUUID().toString();
        String subject = messageSource.getMessage("mail.message",null,locale);
        String text = "http://localhost:8080/email-confirm/" + userAddForm.getEmail() + "/" + token;
        ContactForm contactForm = userAddForm.getContact();
        User user = User
                .builder()
                .name(userAddForm.getName())
                .surname(userAddForm.getSurname())
                .email(userAddForm.getEmail())
                .password(passwordEncoder.encode(userAddForm.getPassword()))
                .userType(UserType.USER)
                .registerDate(new Date())
                .userStatus(UserStatus.INACTIVE)
                .token(token)
                .contact(Contact
                        .builder()
                        .phone(contactForm.getPhone())
                        .address(contactForm.getAddress())
                        .city(City
                                .builder()
                                .id(contactForm.getCityId())
                                .build())
                        .region(Region
                                .builder()
                                .id(contactForm.getRegionId())
                                .build())
                        .build())
                .build();
        userService.add(user,userAddForm.getImage());
        mailService.send(userAddForm.getEmail(),text,subject);
        LOGGER.info("{} saved successfully",user);
        return "redirect:/register?confirm_email";
    }

    @GetMapping("/email-confirm/{email}/{token}")
    public String confirmEmail(@PathVariable("email")String email,
                               @PathVariable("token")String token){
        User user = checkTokenAndEmail(email,token,userService);
        if(user != null){
            user.setUserStatus(UserStatus.ACTIVE);
            user.setToken(null);
            userService.update(user);
            LOGGER.info("{} email confirmed successfully");
            return "redirect:/login?email_success_confirmed";
        }
        return "redirect:/";
    }

    public static User checkTokenAndEmail(String email,String token,UserService userService){
        Optional<User> optionalUser = userService.getByEmail(email);
        User user;
        if(optionalUser.isPresent() && (user = optionalUser.get()).getUserStatus() == UserStatus.INACTIVE){
            String actualToken = user.getToken();
            if(token.equals(actualToken)){
                return user;
            }
        }
        return null;
    }
}
package net.travel.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddForm {

    @Length(min = 2,max = 40)
    private String name;

    @Length(min = 2,max = 50)
    private String surname;

    @Length(min = 9,max = 100)
    private String email;

    @Length(min = 2,max = 40)
    private String password;

    @Length(min = 2,max = 40)
    private String rePassword;

    @Valid
    private ContactForm contact;

    private MultipartFile image;
}
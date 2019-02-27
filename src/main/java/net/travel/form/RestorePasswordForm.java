package net.travel.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestorePasswordForm {

    private String token;

    private String email;

    @Length(min = 2,max = 40)
    private String password;

    @Length(min = 2,max = 40)
    private String rePassword;
}
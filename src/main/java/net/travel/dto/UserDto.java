package net.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travel.model.Contact;
import net.travel.model.enums.UserStatus;
import net.travel.model.enums.UserType;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private int id;

    private String name;

    private String surname;

    private String email;

    private UserType userType;

    private String imgUrl;

    private String registerDate;

    private Contact contact;
}
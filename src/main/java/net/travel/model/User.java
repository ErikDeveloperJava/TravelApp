package net.travel.model;

import lombok.*;
import net.travel.model.enums.UserStatus;
import net.travel.model.enums.UserType;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String imgUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String token;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Contact contact;
}
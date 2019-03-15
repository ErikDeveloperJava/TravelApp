package net.travel.dto;

import lombok.*;
import net.travel.model.Hotel;
import net.travel.model.Place;
import net.travel.model.User;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"sendDate","user",
        "place","hotel"})
public class ReviewDto {

    private int id;

    private String message;

    private String sendDate;

    private int rating;

    private UserDto user;

    private PlaceDto place;

    private HotelDto hotel;
}
package net.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travel.model.Contact;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {

    private int id;

    private String name;

    private String information;

    private String imgUrl;

    private PlaceDto placeDto;

    private Contact contact;
}
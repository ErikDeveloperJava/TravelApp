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
public class PlaceDto {

    private int id;

    private String name;

    private String information;

    private double price;

    private String imgUrl;

    private Contact contact;
}
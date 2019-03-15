package net.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travel.model.Hotel;
import net.travel.model.HotelRoomAttribute;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRoomDto {

    private int id;

    private String name;

    private String information;

    private double price;

    private String imgUrl;

    private int count;

    private int busiedCount;

    private HotelDto hotel;

    private HotelRoomAttribute hotelRoomAttribute;

}
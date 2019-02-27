package net.travel.model;

import lombok.*;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hotel_room_attribute")
public class HotelRoomAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int roomCount;

    private boolean wifi;

    private boolean airConditioner;

    private boolean tv;

    private boolean warmWater;
}
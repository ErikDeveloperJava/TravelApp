package net.travel.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hotel_room")
public class HotelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String information;

    private double price;

    private String imgUrl;

    private int count;

    private int busiedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    @ManyToOne
    private HotelRoomAttribute hotelRoomAttribute;

    @OneToMany(mappedBy = "hotelRoom")
    private List<HotelRoomImage> imageList;
}
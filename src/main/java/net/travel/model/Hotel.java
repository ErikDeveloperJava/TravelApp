package net.travel.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = "hotelRoomList")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String information;

    private String imgUrl;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Place place;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Contact contact;

    @OneToMany(mappedBy = "hotel")
    private List<HotelRoom> hotelRoomList;
}
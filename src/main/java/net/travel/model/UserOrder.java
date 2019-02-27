package net.travel.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_order")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date whenDate;

    private int howManyDays;

    private double price;

    private int adultCount;

    private int childrenCount;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    private HotelRoom hotelRoom;

    @ManyToOne
    private User user;
}
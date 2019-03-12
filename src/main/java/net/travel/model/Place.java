package net.travel.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = "reviewList")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String information;

    private double price;

    private String imgUrl;

    @ManyToOne
    private Contact contact;

    @OneToMany(mappedBy = "place")
    private List<Review> reviewList;
}
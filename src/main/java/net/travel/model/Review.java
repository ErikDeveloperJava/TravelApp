package net.travel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    private int rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Place place;

    @ManyToOne
    private Hotel hotel;
}
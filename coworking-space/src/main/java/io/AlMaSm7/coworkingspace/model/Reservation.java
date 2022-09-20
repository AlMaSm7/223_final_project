package io.AlMaSm7.coworkingspace.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "accepted", nullable = false)
    private int accepted;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    //@JsonManagedReference
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    //@JsonManagedReference
    private User user;
}
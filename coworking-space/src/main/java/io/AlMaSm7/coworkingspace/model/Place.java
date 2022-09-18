package io.AlMaSm7.coworkingspace.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Getter
    @Setter
    private String description;

    @Setter
    @Getter
    private String nr;

    @OneToMany(mappedBy = "place")
    @Getter
    @Setter
    private List<Reservation> reservations;
}
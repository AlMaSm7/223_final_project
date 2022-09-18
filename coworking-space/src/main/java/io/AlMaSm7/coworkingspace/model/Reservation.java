package io.AlMaSm7.coworkingspace.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    private LocalDateTime startDate;

    @Getter
    @Setter
    private LocalDateTime endDate;

    @Getter
    @Setter
    private int accepted;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @Getter
    @Setter
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;
}
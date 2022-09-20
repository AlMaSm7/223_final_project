package io.AlMaSm7.coworkingspace.model;

import com.fasterxml.jackson.annotation.*;
import io.AlMaSm7.coworkingspace.config.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")*/
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user")
    //@JsonBackReference
    //@JsonView(Views.Internal.class)
    private List<Reservation> reservations;
}
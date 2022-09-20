package io.AlMaSm7.coworkingspace.repositories;

import io.AlMaSm7.coworkingspace.model.Place;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    Reservation findByUserId(long id);
    Reservation findByPlaceId(long id);
}

package io.AlMaSm7.coworkingspace.repositories;

import io.AlMaSm7.coworkingspace.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Long> {
}

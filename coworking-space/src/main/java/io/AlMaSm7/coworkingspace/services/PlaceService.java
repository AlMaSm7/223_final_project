package io.AlMaSm7.coworkingspace.services;

import io.AlMaSm7.coworkingspace.model.Place;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.repositories.PlaceRepo;
import io.AlMaSm7.coworkingspace.repositories.ReservationRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceService {
    private final PlaceRepo placeRepo;
    private final ReservationRepo resRepo;

    public List<Place> getPlaces() {
        return placeRepo.findAll();
    }

    public Place addPlace(Place place) {
        placeRepo.save(place);
        return place;
    }

    public Place getPlaceById(long id) {
        Optional<Place> place = placeRepo.findById(id);
        return place.get();
    }

    public Place updatePlace(Place place) {
        Optional<Place> userToUpdate = placeRepo.findById(place.getId());
        Place updatedPlace = null;
        if (userToUpdate.isPresent()){
            updatedPlace = userToUpdate.get();
            updatedPlace.setDescription(place.getDescription());
            updatedPlace.setNr(place.getNr());
            placeRepo.save(updatedPlace);
        }
        return updatedPlace;
    }

    @Transactional
    public Place deletePlace(long id) {
        Optional<Place> place = placeRepo.findById(id);
        if(place.isPresent()){
            Reservation resPlace = resRepo.findByPlaceId(place.get().getId());
            if(resPlace != null){
                resRepo.delete(resPlace);
            }
            placeRepo.delete(place.get());
        }
        return place.get();
    }
}

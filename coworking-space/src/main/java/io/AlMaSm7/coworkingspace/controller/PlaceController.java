package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.Place;
import io.AlMaSm7.coworkingspace.repositories.PlaceRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceController {

    private final PlaceRepo placeRepo;

    @GetMapping("/places")
    public ResponseEntity<?> getPlaces() {
        try {
            List<Place> places = placeRepo.findAll();
            if (places.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok().body(places);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something happened");
        }
    }

    @PostMapping("/places")
    public ResponseEntity<?> addPlace(@RequestBody Place place) {
        try {
            placeRepo.save(place);
            return ResponseEntity.ok().body(place);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not add place");
        }
    }

    @GetMapping("/places/{id}")
    public ResponseEntity<?> getPlaceById(@PathVariable long id) {
        try {
            Optional<Place> foundPlace = placeRepo.findById(id);
            if (foundPlace.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok().body(foundPlace);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not find place");
        }
    }

    @PatchMapping("/places")
    public ResponseEntity<?> updatePlace(@RequestBody Place place) {
        try {
            Optional<Place> userToUpdate = placeRepo.findById(place.getId());
            Place updatedPlace = userToUpdate.get();
            if (updatedPlace == null) return ResponseEntity.notFound().build();
            updatedPlace.setDescription(place.getDescription());
            updatedPlace.setNr(place.getNr());
            updatedPlace.setReservations(place.getReservations());
            placeRepo.save(updatedPlace);
            return ResponseEntity.ok().body(place.getNr() + "Place updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not edit place");
        }
    }

    @DeleteMapping("/places")
    public ResponseEntity<?> deletePlace(@RequestBody Place place) {
        Optional<Place> place1 = placeRepo.findById(place.getId());
        if (place1.isEmpty()) return ResponseEntity.notFound().build();
        // Delete user
        try {
            placeRepo.delete(place1.get());
            return ResponseEntity.ok().body(place1.get().getNr() + " Place deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("There was an issue deleting the place");
        }
    }
}

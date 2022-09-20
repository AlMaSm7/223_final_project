package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.Place;
import io.AlMaSm7.coworkingspace.services.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/places")
    public ResponseEntity<?> getPlaces() {
        try {
            ResponseEntity res;
            List<Place> places = placeService.getPlaces();
            if (places.isEmpty()) {
                res = ResponseEntity.noContent().build();
            } else {
                res = ResponseEntity.ok().body(places);
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occured in the server");
        }
    }

    @PostMapping("/places")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPlace(@RequestBody Place place) {
        ResponseEntity res;
        try {
            Place place1 = placeService.addPlace(place);
            res = ResponseEntity.ok().body(place1);
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not add place");
        }
    }

    @GetMapping("/places/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPlaceById(@PathVariable long id) {
        ResponseEntity res;
        try {
            Place foundPlace = placeService.getPlaceById(id);
            if (foundPlace == null) {
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(foundPlace);
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not find place");
        }
    }

    @PutMapping(value = "/places", produces = "application/json", consumes="application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePlace(@RequestBody Place place) {
        ResponseEntity res;
        try {
            Place place1 = placeService.updatePlace(place);
            if (place1 == null) {
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(place.getNr() + "Place updated successfully");
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not edit place");
        }
    }

    @DeleteMapping("/places/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePlace(@PathVariable long id) {
        ResponseEntity res;
        //Delete place
        try {
            Place place1 = placeService.deletePlace(id);
            if (place1 == null) {
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(place1.getNr() + " Place deleted successfully");
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("There was an issue deleting the place");
        }
    }
}

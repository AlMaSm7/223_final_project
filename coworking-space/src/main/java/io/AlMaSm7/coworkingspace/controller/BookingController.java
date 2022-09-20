package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.config.ControlReservation;
import io.AlMaSm7.coworkingspace.exception.DateException;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.services.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/api/book")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable long id) {
        return ResponseEntity.ok().body(bookingService.getReservationStateById(id));
    }

    @GetMapping
    public ResponseEntity<?> getBookings() {
        List<Reservation> bookings = bookingService.getReservations();
        ResponseEntity res;
        if (bookings.isEmpty()) {
            res = ResponseEntity.noContent().build();
        } else {
            res = ResponseEntity.ok().body(bookings);
        }
        return res;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Reservation reservation) {
        log.info(String.valueOf(reservation));
        try {
            Reservation res = bookingService.createReservation(reservation);
            return ResponseEntity.ok().body(res);
        }catch (DateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/authorize")
    public ResponseEntity<Reservation> authorizeReservation(@RequestBody ControlReservation reservation){
        try {
            ResponseEntity responseEntity;
            Reservation res = bookingService.authorizeReservationProcess(reservation);
            if(res != null){
                responseEntity = ResponseEntity.ok().body(res);
            } else {
                responseEntity = ResponseEntity.notFound().build();
            }
            return responseEntity;
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable long id){
        try{
            ResponseEntity res;
            Reservation reservation = bookingService.cancelReservation(id);
            if(reservation != null){
                res = ResponseEntity.ok().body(reservation);
            } else {
                res = ResponseEntity.notFound().build();
            }
            return res;
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}

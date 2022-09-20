package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.config.ControlReservation;
import io.AlMaSm7.coworkingspace.exception.DateException;
import io.AlMaSm7.coworkingspace.exception.StatusException;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.services.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/api/book")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/status/{id}")
    public ResponseEntity<String> getBookingById(@PathVariable long id) {
        String status = bookingService.getReservationStateById(id);
        if (status == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(bookingService.getReservationStateById(id));

        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
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
        } catch (DateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/authorize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> authorizeReservation(@RequestBody ControlReservation reservation) {
        try {
            ResponseEntity responseEntity;
            Reservation res = bookingService.authorizeReservationProcess(reservation);
            if (res != null) {
                responseEntity = ResponseEntity.ok().body(res);
            } else {
                responseEntity = ResponseEntity.notFound().build();
            }
            return responseEntity;
        } catch (StatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable long id) {
        try {
            ResponseEntity res;
            Reservation reservation = bookingService.cancelReservation(id);
            if (reservation != null) {
                res = ResponseEntity.ok().body(reservation);
            } else {
                res = ResponseEntity.notFound().build();
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

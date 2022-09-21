package io.AlMaSm7.coworkingspace.services;

import io.AlMaSm7.coworkingspace.config.ControlReservation;
import io.AlMaSm7.coworkingspace.exception.DateException;
import io.AlMaSm7.coworkingspace.exception.StatusException;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.repositories.ReservationRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingService {
    private final ReservationRepo reservationRepo;

    public String getReservationStateById(long id) {
        Optional<Reservation> reservation = reservationRepo.findById(id);
        String status = null;
        if(reservation.isPresent()){
            status = setStatus(reservation.get().getAccepted());
        }
        return status;
    }


    public Reservation createReservation(Reservation reservation) throws DateException {
        LocalDateTime startDate = reservation.getStartDate();
        LocalDateTime endDate = reservation.getEndDate();
        if (startDate.getHour() < endDate.getHour()) {
            if (startDate.getHour() >= 7 && startDate.getHour() <= 13) {
                if (endDate.getHour() <= 18) {
                    reservationRepo.save(reservation);
                } else {
                    throw new DateException("This place closes at 19 O'clock");
                }
            } else {
                throw new DateException("Please reserve before 14 O'clock");
            }
        } else {
            throw new DateException("End Date before Start date!");
        }
        return reservation;
    }

    public List<Reservation> getReservations() {
        return reservationRepo.findAll();
    }

    private String setStatus(int status) {
        String result = null;
        if (status == 0) {
            result = "pending";
        } else if (status == 1) {
            result = "accepted";
        } else if( status == 2) {
            result = "denied";
        }
        return result;
    }

    public Reservation authorizeReservationProcess(ControlReservation controlReservation) throws StatusException {
        Optional<Reservation> res = reservationRepo.findById(controlReservation.getId());
        Reservation reservation = null;
        if(res.isPresent()){
            reservation = res.get();
            int status = getStatus(controlReservation.getAnswer());
            reservation.setAccepted(status);
            reservationRepo.save(reservation);
        }
        return reservation;
    }

    public Reservation cancelReservation(long id){
        Optional<Reservation> cancelRes = reservationRepo.findById(id);
        if(cancelRes.isPresent()){
            reservationRepo.delete(cancelRes.get());
            return cancelRes.get();
        } else {
            return null;
        }
    }

    private int getStatus(String req) throws StatusException {
        int result = 0;
        if (req.equals("pending")) {
            result = 0;
        } else if (req.equals("accepted")) {
            result = 1;
        } else if(req.equals("denied")) {
            result = 2;
        } else {
            throw new StatusException("Use these states: pending, accepted, or denied");
        }
        return result;
    }

}

package io.AlMaSm7.coworkingspace;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.AlMaSm7.coworkingspace.config.ControlReservation;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.repositories.PlaceRepo;
import io.AlMaSm7.coworkingspace.repositories.ReservationRepo;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import io.AlMaSm7.coworkingspace.security.JwtServiceHMAC;
import jdk.jfr.Description;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtServiceHMAC jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlaceRepo placeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ReservationRepo reservationRepo;

    private String accessToken;

    @BeforeEach
    public void init() {
        accessToken = jwtService.createNewJWT(UUID.randomUUID().toString(), "1", "email@gmail.com", List.of("ADMIN"));
    }

    @Test
    @Description("Tests if all entries in the inital db are reachable")
    public void allBookingsShouldBeReturnedFromService() throws Exception {
        val response = mockMvc.perform(get("/api/book").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Reservation> reservations = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(1, reservations.size());
    }

    @Test
    @Description("adding a booking with given room and user")
    public void addBookingWithRoom() throws Exception {
        // configure date
        Clock clock = Clock.fixed(Instant.parse("2022-09-20T13:00:00Z"), ZoneId.of("UTC"));
        Clock clockEnd = Clock.fixed(Instant.parse("2022-09-20T18:00:00Z"), ZoneId.of("UTC"));

        LocalDateTime startDateTime = LocalDateTime.now(clock);
        LocalDateTime endDateTime = LocalDateTime.now(clockEnd);

        Reservation reservation = new Reservation();
        reservation.setAccepted(1);
        reservation.setStartDate(startDateTime);
        reservation.setEndDate(endDateTime);
        reservation.setId(2);
        if (placeRepo.findById(1L).isEmpty() || userRepo.findById(1L).isEmpty()) {
            throw new Exception("Data in db couldn't be found");
        }
        reservation.setPlace(placeRepo.findById(2L).get());
        reservation.setUser(userRepo.findById(1L).get());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(reservation);

        val response = mockMvc.perform(post("/api/book").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Reservation res = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(res.getPlace().getId(), 2);
    }

    @Test
    @Description("throws Date Exception, as end date is before start date")
    public void addWithDateException() throws Exception {
        // configure date
        Clock clock = Clock.fixed(Instant.parse("2022-09-20T13:00:00Z"), ZoneId.of("UTC"));
        Clock clockEnd = Clock.fixed(Instant.parse("2022-09-20T12:00:00Z"), ZoneId.of("UTC"));

        LocalDateTime startDateTime = LocalDateTime.now(clock);
        LocalDateTime endDateTime = LocalDateTime.now(clockEnd);

        Reservation reservation = new Reservation();
        reservation.setAccepted(1);
        reservation.setStartDate(startDateTime);
        reservation.setEndDate(endDateTime);
        reservation.setId(1);
        if (placeRepo.findById(1L).isEmpty() || userRepo.findById(1L).isEmpty()) {
            throw new Exception("Data in db couldn't be found");
        }
        reservation.setPlace(placeRepo.findById(1L).get());
        reservation.setUser(userRepo.findById(1L).get());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(reservation);

        mockMvc.perform(post("/api/book").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Test if the admin can update the reservation for the user")
    public void testAdminFunction() throws Exception {

        ControlReservation controlReservation = new ControlReservation();
        controlReservation.setAnswer("denied");
        controlReservation.setId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(controlReservation);

        val res = mockMvc.perform(put("/api/book/authorize").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Reservation reservation = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(reservation.getAccepted(), 2);
    }

    @Test
    @Description("Testing Status Exception if a wrong one is given")
    public void testStatusException() throws Exception {
        ControlReservation controlReservation = new ControlReservation();
        controlReservation.setAnswer("gibberish");
        controlReservation.setId(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(controlReservation);

        val res = mockMvc.perform(put("/api/book/authorize").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Testing if getting status for a member works")
    public void testGetBookingStatus() throws Exception {
        val res = mockMvc.perform(get("/api/book/status/1").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        assertEquals(res.getResponse().getContentAsString(), "pending");
    }

    @Test
    @Description("Testing if getting unknown status for a member throws error")
    public void testGetBookingStatusNotFound() throws Exception {
        val res = mockMvc.perform(get("/api/book/status/67765").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Testing if delete not found works works")
    public void testCancellingBooking() throws Exception {
        val res = mockMvc.perform(get("/api/book/cancel/75466547").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Description("Testing deletion function")
    public void testCancellationOfReservation() throws Exception {
        // configure date
        Clock clock = Clock.fixed(Instant.parse("2022-09-20T7:00:00Z"), ZoneId.of("UTC"));
        Clock clockEnd = Clock.fixed(Instant.parse("2022-09-20T12:00:00Z"), ZoneId.of("UTC"));

        LocalDateTime startDateTime = LocalDateTime.now(clock);
        LocalDateTime endDateTime = LocalDateTime.now(clockEnd);

        Reservation reservation = new Reservation();
        reservation.setAccepted(1);
        reservation.setStartDate(startDateTime);
        reservation.setEndDate(endDateTime);
        reservation.setId(3);
        if (placeRepo.findById(1L).isEmpty() || userRepo.findById(1L).isEmpty()) {
            throw new Exception("Data in db couldn't be found");
        }
        reservation.setPlace(placeRepo.findById(2L).get());
        reservation.setUser(userRepo.findById(1L).get());

        reservationRepo.save(reservation);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String req = ow.writeValueAsString(reservation);

        val res = mockMvc.perform(get("/api/book/cancel/3 ").contentType(MediaType.APPLICATION_JSON)
                        .content(req)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
